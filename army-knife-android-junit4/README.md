# What is this?

this library is support for Android JUnit4 with Kotlin.

You can compatible JUnit4 test code run on JavaVM on PC and Instrumentation on the Android Devices.

# examples

```kotlin

@RunWith(AndroidJUnit4::class)
class HogeTest {
  @Test
  fun fugaTest1() = compatibleTest {
      // run  on JVM and Android Device.
  }

  @Test
  fun fugaTest2() = instrumetationTest {
      // run  on Android Device only.
  }

  @Test
  fun fugaTest3() = localTest {
      // run on JVM on PC only.
  }
}

```
