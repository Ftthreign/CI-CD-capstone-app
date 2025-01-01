# Explanation of Android CI/CD Workflow with GitHub Actions

This is the GitHub Actions workflow for automating CI/CD of an Android app, including the purpose of each keyword and code snippet. Based on workflow file in this repository

---

## Table of Contents

*   [Triggering Events](#triggering-events)
    *   [on Block](#on-block)
*   [Jobs](#jobs)
    *   [Checkout Code](#checkout-code)
    *   [Setup JDK 17](#setup-jdk-17)
    *   [Cache Gradle Dependencies](#cache-gradle-dependencies)
    *   [Load `google-services.json`](#load-google-servicesjson)
    *   [Create `local.properties` with Github Secrets](#create-localproperties-with-github-secrets)
    *   [Grant Execute Permission to `gradlew`](#grant-execute-permission-to-gradlew)
    *   [Build APK](#build-apk)
    *   [Verify APK Output](#verify-apk-output)
    *   [Deploy to Firebase App Distribution](#deploy-to-firebase-app-distribution)
*   [YAML Keywords](#yaml-keywords)

---


## Triggering Events

### `on` Block
Defines the conditions for triggering the workflow.

```yaml
on:
  push:
    branches:
      - master
  pull_request:
    branches:
      - master
```

> **push** : The workflow triggers when a commit push to the branch **master**
>  **pull_request** : The workflow triggers when a pull request targets the **master branch**

## Jobs

### `jobs` Block
Defines the jobs that will be run as part of the workflow. In this case, there is a single job: **build**.

Here the jobs steps based on this repository workflow

1. **Checkout code**
```yaml
- name: Checkout code
  uses: actions/checkout@v3
```

This steps Checks out the repository code so that subsequent steps can access the source files.

2. **Setup JDK 17**
```yaml
- name: Checkout code
  uses: actions/checkout@v3
```

This steps Configures the **JDK version 17** with the `temurin` distribution, which is required for building Android Projects.

3. **Cache Gradle Dependencies**
```yaml
- name: Cache for gradle
  uses: actions/cache@v3
  with:
    path: |
      ~/.gradle/caches
      ~/.gradle/wrapper
    key: ${{ runner.os }}-gradle-${{ hashFiles('**/*.gradle.kts', '**/gradle-wrapper.properties') }}
    restore-keys: |
      ${{ runner.os }}-gradle-
```

This steps Speeds up build by caching **Gradle dependencies**.<br>
For the **Key configuration** is used for unique cache keys based on Gradle configuration files to ensure cache consistency.

4. **Load `google-services.json`**
```yaml
- name: Load Google Service file
  env:
    DATA: ${{ secrets.GOOGLE_SERVICES_JSON }}
  run: echo $DATA | base64 -di > app/google-services.json
```

This steps Decodes the Firebase configurations file from **base64** secret and save it to the `app` folder.

5. **Create `local.properties` with Github Secrets**
```yaml
- name: Create local.properties with secrets
  run: |
    echo "API_URL=${{ secrets.API_URL }}" >> local.properties
    echo "GOOGLE_MAPS_API_KEY=${{ secrets.GOOGLE_MAPS_API_KEY }}" >> local.properties
    echo "GOOGLE_DIRECTION_API_KEY=${{ secrets.GOOGLE_DIRECTION_API_KEY }}" >> local.properties
```

This steps Dynamically creates a `local.properties` file and injects the github secret values for API keys or sensitives environtment data.

6. **Grant Execute Permission to `gradlew`**
```yaml
- name: Grant execute permission to gradlew
  run: chmod +x ./gradlew
```

This steps Ensures the Gradle wrapper script `gradlew` has execute permissions, required to run Gradle commands.

7. **Build APK**
```yaml
- name: Build Debug APK
  run: ./gradlew assembleDebug --stacktrace --info
```

This steps Compiles the project and generates a debug APK using Gradle


8. **Verify APK Output**
```yaml
- name: Debug Output
  run: ls -R app/build/outputs/apk/debug/app-debug.apk
```

This steps Lists the APK fiel path to confirm that APK was successfully generated.

9. **Deploy to Firebase App Distribution**
```yaml
- name: Deploy to Firebase (Debug)
  uses: wzieba/Firebase-Distribution-Github-Action@v1
  with:
    appId: ${{ secrets.FIREBASE_APP_ID }}
    serviceCredentialsFileContent: ${{ secrets.CREDENTIAL_FILE_CONTENT }}
    groups: belajar-ci-cd
    file: app/build/outputs/apk/debug/app-debug.apk
```

This steps Uploads the generated APK to Firebase App Distribution for testing.
This is the parameter that need to config :
> - `appId` : **Firebase App ID** from the project settings
> - `serviceCredentialsFileContent` : Encoded Firebase service account credentials.
> - `groups` : Specifies tester groups to distribute the APK.
> - `file` : Path to the APK file.


## YAML Keywords

**WIP**