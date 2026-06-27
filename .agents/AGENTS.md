# Custom Agent Rules

## Git Workflow Constraints
- **Feature Branches**: Always use a feature branch to make code modifications. Do not commit directly to the `main` branch.
- **Merge and Push Confirmation**: Only merge a feature branch into the `main` branch, or push the `main` branch to the remote repository, after obtaining explicit user confirmation.
- **Squashing on Push**: When pushing the `main` branch to remote, squash all commits on the `main` branch with the commit message "Initial commit of KMP design system" before pushing.
- **Branch Cleanup**: Delete the local and remote feature branch after it has been merged into the `main` branch.

## Build Configuration Constraints
- **Local Builds**: When building or running tasks locally (such as building the host applications, compile scripts, or libraries), always use the debug configurations (e.g. Gradle `assembleDebug` tasks, or Xcode schemes/commands set to `Debug` configuration) unless explicitly requested otherwise.
