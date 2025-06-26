# ✍️ RichTextEditor for Android

A powerful and easy-to-integrate **Rich Text Editor** for Android written in Kotlin.  
Supports all essential formatting features and rich media insertion like images, YouTube videos, audio, and more.

---

## 🎥 Demo

https://user-images.githubusercontent.com/your-github-id/demo-video.mp4  
<!-- Or YouTube video embed: -->
[![Watch the demo](https://img.youtube.com/vi/YOUTUBE_VIDEO_ID/0.jpg)](https://www.youtube.com/watch?v=YOUTUBE_VIDEO_ID)

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
