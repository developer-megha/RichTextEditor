# ✍️ RichTextEditor for Android

A powerful and easy-to-integrate **Rich Text Editor** for Android written in Kotlin.  
Supports all essential formatting features and rich media insertion like images, YouTube videos, audio, and more.

---

## 🎥 Demo Video

👉 [Click to Watch the Demo](https://github.com/developer-megha/RichTextEditor/blob/main/app/src/main/assets/demo_video.mp4?raw=true)

---

## 🚀 Features

- Font Family
- Font Size
- **Bold**, *Italic*, _Underline_, ~~Strikethrough~~
- Subscript / Superscript
- Numbering & Bullets
- Text Alignment: Left, Right, Center, Justify
- Font Color & Background Color
- **Clear Formatting**
- Blockquote
- Undo / Redo
- Indent / Outdent
- Insert:
  - 🔗 Hyperlinks
  - 🖼️ Image
  - 📹 YouTube Video
  - 🎥 Video
  - 🎧 Audio
  - ☑️ Checkboxes

---

## 🛠️ Installation

### Step 1: Add JitPack to your `settings.gradle.kts` 
```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

### Step 2: Add the dependency
```kotlin
dependencies {
    implementation("com.github.developer-megha:RichTextEditor:1.0") // latest version
}


