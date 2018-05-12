# andriod-utils
Templates and boilerplate code for most used components in Android. 

# Installation

Gradle:

Step 1: Add it in your root build.gradle at the end of repositories:
```groovy 
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
Step 2: Add the dependency
```groovy 
	dependencies {
	        implementation 'com.github.CrinoidTechnologies:andriod-utils:v0.5.1'
	}
 ``` 
Maven:

```xml
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```
Step 2. Add the dependency

```xml
	<dependency>
	    <groupId>com.github.CrinoidTechnologies</groupId>
	    <artifactId>andriod-utils</artifactId>
	    <version>v0.5.1</version>
	</dependency>
```
