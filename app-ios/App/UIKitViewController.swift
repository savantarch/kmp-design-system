import Shared
import UIKit

final class UIKitViewController: UIViewController {
    private let logoImageView = UIImageView()
    private let titleLabel = UILabel()
    private let welcomeLabel = UILabel()
    private let cardView = UIView()

    private let usernameLabel = UILabel()
    private let usernameTextField = PaddingTextField()
    private let emailLabel = UILabel()
    private let emailTextField = PaddingTextField()

    private let switchContainer = UIStackView()
    private let switchLabel = UILabel()
    private let notificationSwitch = UISwitch()

    private let actionButton = UIButton(type: .system)

    private var cardStackTopConstraint: NSLayoutConstraint?
    private var cardStackBottomConstraint: NSLayoutConstraint?
    private var cardStackLeadingConstraint: NSLayoutConstraint?
    private var cardStackTrailingConstraint: NSLayoutConstraint?

    override func viewDidLoad() {
        super.viewDidLoad()
        setupUI()
        applyThemeStyles()
        updateStrings()
    }

    private func updateStrings() {
        titleLabel.text = "Account Settings"
        welcomeLabel.text = String(format: AppStrings.welcomeUser.localized, "UIKit Developer")
        actionButton.setTitle(AppStrings.btnExplore.localized, for: .normal)
    }

    private func applyThemeStyles() {
        let theme = AppThemeSwift.shared
        view.backgroundColor = theme.colors.background.uiColor

        // Title & Welcome Labels
        titleLabel.textColor = theme.colors.onSurface.uiColor
        titleLabel.font = theme.typography.titleLargeFont
        welcomeLabel.textColor = theme.colors.primary.uiColor
        welcomeLabel.font = theme.typography.bodyMediumFont.bold

        // Card Container
        cardView.backgroundColor = theme.colors.surface.uiColor
        cardView.layer.cornerRadius = CGFloat(theme.shapes.medium.topLeft)

        // Labels
        usernameLabel.textColor = theme.colors.onSurface.uiColor
        usernameLabel.font = theme.typography.labelSmallFont
        emailLabel.textColor = theme.colors.onSurface.uiColor
        emailLabel.font = theme.typography.labelSmallFont

        // Text Fields
        let fieldBg = theme.colors.surfaceVariant.uiColor
        let strokeColor = theme.colors.primary.uiColor.cgColor

        usernameTextField.backgroundColor = fieldBg
        usernameTextField.textColor = theme.colors.onSurface.uiColor
        usernameTextField.font = theme.typography.bodyMediumFont
        usernameTextField.layer.borderColor = strokeColor
        usernameTextField.layer.cornerRadius = 8

        emailTextField.backgroundColor = fieldBg
        emailTextField.textColor = theme.colors.onSurface.uiColor
        emailTextField.font = theme.typography.bodyMediumFont
        emailTextField.layer.borderColor = strokeColor
        emailTextField.layer.cornerRadius = 8

        // Switch
        switchLabel.textColor = theme.colors.onSurface.uiColor
        switchLabel.font = theme.typography.bodyMediumFont
        notificationSwitch.onTintColor = theme.colors.secondary.uiColor

        // Button
        actionButton.backgroundColor = theme.colors.primary.uiColor
        actionButton.setTitleColor(theme.colors.onPrimary.uiColor, for: .normal)
        actionButton.layer.cornerRadius = CGFloat(theme.shapes.small.topLeft)
        actionButton.titleLabel?.font = theme.typography.labelMediumFont.bold

        logoImageView.tintColor = theme.colors.primary.uiColor

        let padding = CGFloat(24)
        cardStackTopConstraint?.constant = padding
        cardStackBottomConstraint?.constant = -padding
        cardStackLeadingConstraint?.constant = padding
        cardStackTrailingConstraint?.constant = -padding
    }

    override func traitCollectionDidChange(_ previousTraitCollection: UITraitCollection?) {
        super.traitCollectionDidChange(previousTraitCollection)
        applyThemeStyles()
        updateStrings()
    }

    private func setupUI() {
        let mainStack = UIStackView()
        mainStack.axis = .vertical
        mainStack.alignment = .center
        mainStack.spacing = 16
        mainStack.translatesAutoresizingMaskIntoConstraints = false
        view.addSubview(mainStack)

        logoImageView.image = AppImages.logo.uiImage
        logoImageView.contentMode = .scaleAspectFit
        logoImageView.translatesAutoresizingMaskIntoConstraints = false
        logoImageView.widthAnchor.constraint(equalToConstant: 48).isActive = true
        logoImageView.heightAnchor.constraint(equalToConstant: 48).isActive = true

        titleLabel.textAlignment = .center

        welcomeLabel.textAlignment = .center
        welcomeLabel.numberOfLines = 0

        cardView.layer.shadowColor = UIColor.black.cgColor
        cardView.layer.shadowOpacity = 0.05
        cardView.layer.shadowOffset = CGSize(width: 0, height: 4)
        cardView.layer.shadowRadius = 8
        cardView.translatesAutoresizingMaskIntoConstraints = false

        let cardStack = UIStackView()
        cardStack.axis = .vertical
        cardStack.alignment = .fill
        cardStack.spacing = 14
        cardStack.translatesAutoresizingMaskIntoConstraints = false
        cardView.addSubview(cardStack)

        // Divider
        let divider = UIView()
        divider.translatesAutoresizingMaskIntoConstraints = false
        divider.heightAnchor.constraint(equalToConstant: 1).isActive = true
        divider.backgroundColor = UIColor.placeholderText.withAlphaComponent(0.2)

        // Text Fields Configuration
        usernameTextField.text = "uikit_developer"
        usernameTextField.layer.borderWidth = 1
        usernameTextField.translatesAutoresizingMaskIntoConstraints = false
        usernameTextField.heightAnchor.constraint(equalToConstant: 44).isActive = true

        emailTextField.text = "developer@savantarch.com"
        emailTextField.layer.borderWidth = 1
        emailTextField.translatesAutoresizingMaskIntoConstraints = false
        emailTextField.heightAnchor.constraint(equalToConstant: 44).isActive = true

        // Switch container layout
        switchContainer.axis = .horizontal
        switchContainer.alignment = .center
        switchContainer.distribution = .fill
        switchLabel.text = "Receive notifications"

        switchContainer.addArrangedSubview(switchLabel)
        switchContainer.addArrangedSubview(notificationSwitch)

        // Action Button layout
        actionButton.translatesAutoresizingMaskIntoConstraints = false
        actionButton.heightAnchor.constraint(equalToConstant: 48).isActive = true

        // Stack views setup
        cardStack.addArrangedSubview(welcomeLabel)
        cardStack.addArrangedSubview(divider)
        cardStack.addArrangedSubview(usernameLabel)
        cardStack.addArrangedSubview(usernameTextField)
        cardStack.addArrangedSubview(emailLabel)
        cardStack.addArrangedSubview(emailTextField)
        cardStack.addArrangedSubview(switchContainer)
        cardStack.addArrangedSubview(actionButton)

        cardStack.setCustomSpacing(4, after: usernameLabel)
        cardStack.setCustomSpacing(4, after: emailLabel)

        let padding = CGFloat(24)
        let top = cardStack.topAnchor.constraint(equalTo: cardView.topAnchor, constant: padding)
        let bottom = cardStack.bottomAnchor.constraint(equalTo: cardView.bottomAnchor, constant: -padding)
        let leading = cardStack.leadingAnchor.constraint(equalTo: cardView.leadingAnchor, constant: padding)
        let trailing = cardStack.trailingAnchor.constraint(equalTo: cardView.trailingAnchor, constant: -padding)

        self.cardStackTopConstraint = top
        self.cardStackBottomConstraint = bottom
        self.cardStackLeadingConstraint = leading
        self.cardStackTrailingConstraint = trailing

        NSLayoutConstraint.activate([top, bottom, leading, trailing])

        mainStack.addArrangedSubview(logoImageView)
        mainStack.addArrangedSubview(titleLabel)
        mainStack.addArrangedSubview(cardView)

        NSLayoutConstraint.activate([
            mainStack.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            mainStack.centerYAnchor.constraint(equalTo: view.centerYAnchor),
            mainStack.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 24),
            mainStack.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -24),

            cardView.leadingAnchor.constraint(equalTo: mainStack.leadingAnchor),
            cardView.trailingAnchor.constraint(equalTo: mainStack.trailingAnchor),
        ])
    }
}

// Subclass to easily provide internal bounds padding inside UITextField
class PaddingTextField: UITextField {
    private let padding = UIEdgeInsets(top: 0, left: 12, bottom: 0, right: 12)

    override func textRect(forBounds bounds: CGRect) -> CGRect {
        return bounds.inset(by: padding)
    }

    override func placeholderRect(forBounds bounds: CGRect) -> CGRect {
        return bounds.inset(by: padding)
    }

    override func editingRect(forBounds bounds: CGRect) -> CGRect {
        return bounds.inset(by: padding)
    }
}

extension UIKitViewController: ThemeUpdatable {
    func themeDidChange() {
        applyThemeStyles()
        updateStrings()
    }
}

extension UIFont {
    var bold: UIFont {
        if let descriptor = fontDescriptor.withSymbolicTraits(.traitBold) {
            return UIFont(descriptor: descriptor, size: 0)
        }
        return self
    }
}
