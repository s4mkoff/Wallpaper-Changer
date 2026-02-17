This is multiplatform application for changing wallpaper
Currently only for Android/Linux(hyprland via hyprpaper)
Android has support for background Worker which will execute wallpaper change one time in 15 minutes(But currently sometimes worker crush, and will not execute until scheduled again)
On Linux if builded in AppImage you can launch via terminal without parameters to open gui to change manually, and save API parameters(all data saves in HOME/.wallpaperChanger) or run with -service parameter to change wallpaper with configured API parameters(default if not configured)