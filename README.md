# pasrijoo-map

Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:


	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
  
  Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.farshid7:pasrijoo-map:1.0'
          implementation 'org.osmdroid:osmdroid-android:5.6.5'

	}
