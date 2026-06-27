# Integration with Coil 3 (Image Loading Library)

The **Resource Contracts** pattern decouples the design system from third-party image loading frameworks. This ensures the `:design-system` library remains lightweight and dependency-free. 

If your application uses **Coil 3** for advanced image loading (e.g., asynchronous remote loading, memory/disk caching, transitions, or circular cropping), you can easily bridge `DsImages` with Coil using two different patterns.

---

## Pattern A: Using `toCoilModel()` (Built-in & Direct)

We provide a built-in platform-agnostic helper function `DsImages.toCoilModel()` inside the design system:

```kotlin
@Composable
expect fun DsImages.toCoilModel(): Any
```

* **On Android**: It resolves the dynamic themed attribute ID and returns the raw drawable resource ID (`Int`).
* **On iOS**: It retrieves the compiled native image from our dynamic bundle, converts it to lossless PNG data, and returns a `ByteArray` (which Coil natively decodes and renders on iOS).

### Usage:
Because Coil's `AsyncImage` model accepts `Any?`, you can pass this helper directly in your common Compose Multiplatform screens without adding any platform-specific code or Coil dependencies to the design system:

```kotlin
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.savantarch.design.DsImages
import com.savantarch.shared.AppImages

@Composable
fun CompanyLogoHeader() {
    AsyncImage(
        model = AppImages.LOGO.toCoilModel(),
        contentDescription = "Company logo loaded via Coil",
        modifier = Modifier.size(64.dp)
    )
}
```

---

## Pattern B: Creating a Custom Coil 3 Mapper (For Direct Passing)

If you want the ultimate developer experience, you can register a custom `Mapper` in your client-side application's `ImageLoader`. This allows developers to pass your `DsImages` keys **directly** as the model:

```kotlin
AsyncImage(
    model = AppImages.LOGO, // Pass the enum key directly!
    contentDescription = "Branding"
)
```

### 1. Implement the Custom Mapper in your App module

Since mapping a resource contract is platform-specific, define the custom mapper using Kotlin's `expect`/`actual` pattern in your application's source sets:

#### In `commonMain`:
```kotlin
import coil3.map.Mapper
import coil3.request.Options
import com.savantarch.design.DsImages

expect class DsImageMapper : Mapper<DsImages, Any> {
    override fun map(data: DsImages, options: Options): Any?
}

class DsImageMapperFactory : Mapper.Factory<DsImages, Any> {
    override fun create(data: Any, options: Options): Mapper<DsImages, Any>? {
        return if (data is DsImages) DsImageMapper() else null
    }
}
```

#### In `androidMain`:
```kotlin
import coil3.map.Mapper
import coil3.request.Options
import com.savantarch.design.AndroidDsImages
import com.savantarch.design.DsImages

actual class DsImageMapper : Mapper<DsImages, Any> {
    actual override fun map(data: DsImages, options: Options): Any? {
        val androidImage = data as? AndroidDsImages ?: return null
        val context = options.context
        
        // Resolve dynamic themed ?attr/... to raw Android drawable resource ID (Int)
        val attrId = androidImage.toAttrId()
        return context.getDrawableIdFromAttr(attrId) 
    }
}
```

#### In `iosMain`:
```kotlin
import coil3.map.Mapper
import coil3.request.Options
import com.savantarch.design.IosDsImages
import com.savantarch.design.DsImages
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.usePinned
import kotlinx.cinterop.addressOf
import platform.UIKit.UIImagePNGRepresentation
import platform.posix.memcpy

@OptIn(ExperimentalForeignApi::class)
actual class DsImageMapper : Mapper<DsImages, Any> {
    actual override fun map(data: DsImages, options: Options): Any? {
        val iosImage = data as? IosDsImages ?: return null
        val uiImage = iosImage.uiImage
        
        // Convert UIImage to ByteArray because Coil 3 natively decodes ByteArray on iOS
        val dataRepresentation = UIImagePNGRepresentation(uiImage) ?: return null
        val bytes = ByteArray(dataRepresentation.length.toInt())
        bytes.usePinned { pinned ->
            memcpy(pinned.addressOf(0), dataRepresentation.bytes, dataRepresentation.length)
        }
        return bytes
    }
}
```

---

### 2. Register the Custom Mapper Factory

When initializing your global or local Coil `ImageLoader` (during app startup, e.g., in `Application.onCreate()` on Android or Compose entry points on iOS), append the factory to Coil's `ComponentRegistry`:

```kotlin
import coil3.ImageLoader

val imageLoader = ImageLoader.Builder(context)
    .components {
        add(DsImageMapperFactory()) // Register our custom contract mapper!
    }
    .build()
```

This clean architecture keeps the core design system completely free of any Coil 3 library dependencies while unlocking maximum ergonomics for client developers.
