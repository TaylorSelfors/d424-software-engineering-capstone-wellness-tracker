#!/bin/bash

echo "🛠️ Building APK..."
./gradlew assembleRelease

APK_PATH="app/build/outputs/apk/release/app-release.apk"
DEST_APK="WellnessTracker-v2.apk"

if [ -f "$APK_PATH" ]; then
  echo "✅ APK built successfully."
  cp "$APK_PATH" "$DEST_APK"
  echo "✅ Copied APK to project root as $DEST_APK"
else
  echo "❌ APK not found. Build failed."
  exit 1
fi

echo "📂 Adding and committing changes to working-branch..."
git add .
git commit -m "Build v2: fixed crash bugs, time input, and PDF generation"
git push origin working-branch
echo "✅ Pushed to GitLab: working-branch"

echo ""
echo "🚀 NEXT STEPS (manual):"
echo "1. Go to GitHub → Create new release v2.0 → Upload $DEST_APK"
echo "2. Update GitHub Pages if needed"
echo "3. Confirm updated APK installs and runs cleanly"
