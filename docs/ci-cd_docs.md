# Explanation of Android CI/CD Workflow with GitHub Actions

This is the GitHub Actions workflow for automating CI/CD of an Android app, including the purpose of each keyword and code snippet. Based on workflow file in this repository

---

## Table of Contents

*   [Triggering Events](#triggering-events)
    *   [`on` Block](#on-block)
*   [Jobs](#jobs)
    *   [`jobs` Block](#jobs-block)
    *   [1. Checkout Code](#1-checkout-code)
    *   [2. Setup JDK 17](#2-setup-jdk-17)
    *   [3. Cache Gradle Dependencies](#3-cache-gradle-dependencies)
    *   [4. Load `google-services.json`](#4-load-google-servicesjson)
    *   [5. Create `local.properties` with GitHub Secrets](#5-create-localproperties-with-github-secrets)
    *   [6. Grant Execute Permission to `gradlew`](#6-grant-execute-permission-to-gradlew)
    *   [7. Build APK](#7-build-apk)
    *   [8. Verify APK Output](#8-verify-apk-output)
    *   [9. Deploy to Firebase App Distribution](#9-deploy-to-firebase-app-distribution)
*   [YAML Keywords](#yaml-keywords)
*   [Firebase Parameters](#firebase-parameters)

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

> **push** : The workflow triggers when a commit push to the branch **master** <br>
>  **pull_request** : The workflow triggers when a pull request targets the **master branch**

## Jobs

### `jobs` Block
Defines the jobs that will be run as part of the workflow. In this case, there is a single job: **build**.

Here the jobs steps based on this repository workflow

##### 1. **Checkout code**
```yaml
- name: Checkout code
  uses: actions/checkout@v3
```

This steps Checks out the repository code so that subsequent steps can access the source files.

##### 2. **Setup JDK 17**
```yaml
- name: Checkout code
  uses: actions/checkout@v3
```

This steps Configures the **JDK version 17** with the `temurin` distribution, which is required for building Android Projects.

##### 3. **Cache Gradle Dependencies**
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

##### 4. **Load `google-services.json`**
```yaml
- name: Load Google Service file
  env:
    DATA: ${{ secrets.GOOGLE_SERVICES_JSON }}
  run: echo $DATA | base64 -di > app/google-services.json
```

This steps Decodes the Firebase configurations file from **base64** secret and save it to the `app` folder.

##### 5. **Create `local.properties` with Github Secrets**
```yaml
- name: Create local.properties with secrets
  run: |
    echo "API_URL=${{ secrets.API_URL }}" >> local.properties
    echo "GOOGLE_MAPS_API_KEY=${{ secrets.GOOGLE_MAPS_API_KEY }}" >> local.properties
    echo "GOOGLE_DIRECTION_API_KEY=${{ secrets.GOOGLE_DIRECTION_API_KEY }}" >> local.properties
```

This steps Dynamically creates a `local.properties` file and injects the github secret values for API keys or sensitives environtment data.

##### 6. **Grant Execute Permission to `gradlew`**
```yaml
- name: Grant execute permission to gradlew
  run: chmod +x ./gradlew
```

This steps Ensures the Gradle wrapper script `gradlew` has execute permissions, required to run Gradle commands.

##### 7. **Build APK**
```yaml
- name: Build Debug APK
  run: ./gradlew assembleDebug --stacktrace --info
```

This steps Compiles the project and generates a debug APK using Gradle


##### 8. **Verify APK Output**
```yaml
- name: Debug Output
  run: ls -R app/build/outputs/apk/debug/app-debug.apk
```

This steps Lists the APK fiel path to confirm that APK was successfully generated.

##### 9. **Deploy to Firebase App Distribution**
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

Below is the keyword that used in this repository workflows

# Explanation of YAML Keywords

Below is the explanation of the keywords used in the provided YAML file for Android CI/CD workflow.

## 1. `name`
- **Function**: Provides a descriptive name for the workflow.
- **Example**: `Android CI/CD` is the workflow name.

---

## 2. `on`
- **Function**: Specifies the events or conditions that trigger the workflow.
- **Details**:
    - `push`: The workflow runs when code is pushed to a specific branch.
    - `pull_request`: The workflow runs when a pull request is created for a specific branch.
- **Example**: The workflow triggers on `push` or `pull_request` events targeting the `master` branch.

---

## 3. `jobs`
- **Function**: Defines the jobs to be executed in the workflow. Each job runs independently in a separate environment.

---

## 4. `build`
- **Function**: The name of the job being executed.
- **Example**: This job is named `Build and Deploy APK to Firebase`.

---

## 5. `runs-on`
- **Function**: Specifies the type of runner or operating system to execute the job.
- **Example**: Uses a runner with the `ubuntu-latest` operating system.

---

## 6. `steps`
- **Function**: Contains a list of sequential steps to execute within the job.

---

## 7. `uses`
- **Function**: Utilizes predefined actions or plugins from the GitHub Actions Marketplace.
- **Examples**:
    - `actions/checkout@v3`: Checks out the repository code.
    - `actions/setup-java@v3`: Sets up the Java Development Kit (JDK).
    - `actions/cache@v3`: Configures a cache for Gradle to speed up the build process.
    - `wzieba/Firebase-Distribution-Github-Action@v1`: Deploys the APK to Firebase App Distribution.

---

## 8. `run`
- **Function**: Executes shell commands directly on the runner.
- **Examples**:
    - `chmod +x ./gradlew`: Grants execution permission to the `gradlew` file.
    - `./gradlew assembleDebug --stacktrace --info`: Builds the APK using Gradle.

---

## 9. `env`
- **Function**: Defines environment variables for a specific step.
- **Example**:
    - `DATA: ${{ secrets.GOOGLE_SERVICES_JSON }}`: Retrieves the value of a secret stored in the repository.

---

## 10. `with`
- **Function**: Passes additional parameters to the action used in the step.
- **Examples**:
    - `java-version: 17`: Specifies the JDK version.
    - `appId: ${{ secrets.FIREBASE_APP_ID }}`: Provides the Firebase App ID.

---

## 11. `key`
- **Function**: Defines a unique key for caching. The cache is reused if the same key is found.
- **Example**:
    - `key: ${{ runner.os }}-gradle-${{ hashFiles('**/*.gradle.kts', '**/gradle-wrapper.properties') }}`: Creates a key based on the OS and Gradle configuration files.

---

## 12. `restore-keys`
- **Function**: Specifies fallback keys for restoring the cache if the primary key is not found.
- **Example**:
    - `restore-keys: ${{ runner.os }}-gradle-`: Restores cache matching the OS.

---

## 13. `secrets`
- **Function**: Stores sensitive information like API keys, credentials, or other confidential configurations.
- **Example**:
    - `secrets.GOOGLE_SERVICES_JSON`: Retrieves the `google-services.json` file.

This YAML is designed to automate the CI/CD process for an Android application, from code checkout and building the APK to distributing it via Firebase App Distribution.

--------------

The `Firebase-Distribution-Github-Action` plugin uses several parameters to configure and deploy the APK to Firebase App Distribution. Below are the explanations for the key parameters:

### 1. `appId`
- **Function**: Specifies the Firebase App ID where the APK will be uploaded.
- **Details**: The App ID uniquely identifies your application in the Firebase project.
- **Example**:
  ```yaml
  appId: ${{ secrets.FIREBASE_APP_ID }}
  ```

### 2. `serviceCredentialsFileContent`
- **Function**: Provides the service account credentials required for authentication with Firebase
- **Example**:
  ```yaml
  serviceCredentialsFileContent: ${{ secrets.CREDENTIAL_FILE_CONTENT }}
  ```

### 3. `groups`

- **Function**: Specifies the tester groups in Firebase App Distribution who will receive the deployed APK
- **Details** : The testers in the specified groups will receive email invitations to download and test the application
- **Example**:
  ```yaml
  groups: belajar-ci-cd
  ```

### 4. `file`
- **Function**: Specifies the file path of the APK to be uploaded to Firebase App Distribution
- **Details** : The output file generated after the build proces
- **Example**:
  ```yaml
  file: app/build/outputs/apk/debug/app-debug.apk

  ```