#!/bin/bash
set -e

echo "[*] Building unsigned APK via apktool..."
apktool b -o unsigned_siller.apk .

echo "[*] Signing APK with debug keystore..."
jarsigner -keystore ~/.android/debug.keystore -storepass android -keypass android -sigalg SHA1withRSA -digestalg SHA1 unsigned_siller.apk androiddebugkey

echo "[*] Aligning final APK package..."
zipalign -v 4 unsigned_siller.apk final_siller.apk

echo "[+] Build complete: final_siller.apk"
