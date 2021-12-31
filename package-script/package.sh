#!/bin/bash
scp ../target/image-app.jar ../package-content;
jpackage --type dmg -i ../package-content --name ImageApp --app-version 1.0 --mac-package-name ImageApp --mac-package-identifier ImageApp  --main-jar image-app.jar