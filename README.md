![Static Badge](https://img.shields.io/badge/Version-1.0.0-green?style=for-the-badge)
![Code size](https://img.shields.io/github/languages/code-size/wapatali/jbencode?style=for-the-badge)
![License](https://img.shields.io/github/license/wapatali/jbencode?style=for-the-badge)

# JBencode

_JBencode_ is a small library written in Java that lets you encode and decode according to the specifications of the [Bencode](https://wiki.theory.org/BitTorrentSpecification#Bencoding) algorithm.
It is designed to be easy to learn and use, with a minimalist and lightweight implementation.

## Installation

The examples below use _Gradle_ as a project manager.
The library is published in the _Github Packages_ registry and must therefore be declared in your `build.gradle` file:

```groovy
repositories {
    mavenCentral()
    maven {
        url = uri("https://maven.pkg.github.com/wapatali/jbencode")
    }
}
```

Then, simply add the following dependency:

```groovy
dependencies {
    implementation 'io.kukua:jbencode:1.0.0'
    // others dependencies ...
}
```

## Examples

JBencode has two functions, one for encoding and another for decoding :

```java
static public <T> String encode(T input) throws InvalidInputException {}

static public Object decode(String input) throws InvalidInputException {}
```

According to the algorithm specifications, 4 types of values can be encoded: an integer, a string, a list or a dictionary. 
The encoding function therefore accepts a parameter of type `Long`, `String`, `List` or `SortedMap`:

```java
String integerEncoded = Jbencode.encode(10L);                             // "i10e"

String stringEncoded = Jbencode.encode("foo");                            // "3:foo"

String listEncoded = Jbencode.encode(List.of(10L, "foo"));                // "li10e3:fooe"

String dictEncoded = Jbencode.encode(new TreeMap<>(Map.of("foo", 10L)));  // "d3:fooi10ee"
```

Decoding accepts a string as parameter and returns an object of type `Long`, `String`, `List` or `SortedMap`.
The first possible value is returned, the rest of the string is ignored. For example:

```java
Long integer = (Long) Jbencode.decode("i10e");                                                  // 10

Long otherInteger = (Long) Jbencode.decode("i10e3:foo");                                        // 10 (ignores "foo")

String string = (String) Jbencode.decode("3:foo");                                              // "foo"

List<Object> list = (List<Object>) Jbencode.decode("li10e3:fooe");                              // [10, "foo"]

SortedMap<String, Object> dict = (SortedMap<String, Object>) Jbencode.decode("d3:foo3:bare");   // {"foo": "bar"}
```

## License

JBencode is licensed under the [GNU General Public License v3.0](https://www.gnu.org/licenses/gpl-3.0.en.html).