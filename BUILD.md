# Guide to Create a Release Build in Android Studio

This guide outlines how to generate a release build of Sedcake using the Android Studio interface.

## Prerequisites

- **JDK 17** — Ensure that JDK 17 is installed and configured, as the project is compiled with this version.
- ![JDK 17](sources/wiki/release/build-0.png)

## Steps to Create a Release Build

### Step 1: Configure the Keystore

1. **Open Build Settings:**
   - In Android Studio, go to `Build > Generate Signed Bundle / APK...`.
   - ![Build Settings](sources/wiki/release/build-1.png)

2. **Create a Keystore:**
   - Select the `APK` option and click `Next`.
   - ![APK Selection](sources/wiki/release/build-2.png)
   - Click on `Create new...` to start creating a new keystore.
   - ![Create Keystore](sources/wiki/release/build-3.png)
   - Fill in the required details such as Alias, Keystore Password, Key Password, and personal certificate information.
   - ![Keystore Details](sources/wiki/release/build-4.png)
   - Click `OK` to save the keystore.

### Step 2: Configure and Generate the Release Build

3. **Select the Keystore:**
   - Choose the keystore you created, entering the Alias and Passwords accordingly.
   - ![Keystore Selection](sources/wiki/release/build-5.png)

4. **Choose Build Type and Configure Options:**
   - Select `release` as the build type and confirm the settings.
   - Click `Next` and then `Create`.
   - ![Build Configuration](sources/wiki/release/build-6.png)

### Step 3: Locate the Generated APK

5. **Build Completion:**
   - A notification will indicate that the build was successfully completed. Click `locate` to open the folder where the APK was saved.
   - ![Build Completed](sources/wiki/release/build-7.png)
   - You will find the APK in the specified folder.
   - ![Locate APK](sources/wiki/release/build-8.png)

## Conclusion

Following these steps, you will have generated a signed APK of your project, ready for distribution or installation on devices. This process ensures that you have full control over the creation of builds for your application, which is important for applications dealing with sensitive information.
