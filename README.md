# JBencode

_JBencode_ is a small library written in Java that lets you encode and decode according to the specifications of the [Bencode](https://en.wikipedia.org/wiki/Bencode) algorithm.
There are already enough libraries for this, and _JBencode_ is not intended to replace them. It's just a simple implementation proposal.

## Installation

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
    implementation 'io.kukua:jbencode:0.2.0'
    // others dependencies ...
}
```

## Usage

JBencode has two functions, one for encoding and another for decoding :

```java
public <T> String encode(T input) throws InvalidInputException {}

public Object decode(String input) throws InvalidInputException {}
```

According to the algorithm specifications, 4 types of values can be encoded: an integer, a string, a list or a dictionary. 
The encoding function therefore accepts a parameter of type `Long`, `String`, `List` or `SortedMap`:

```java
String integerEncoded = jbencode.encode(10L);                             // "i10e"

String stringEncoded = jbencode.encode("foo");                            // "3:foo"

String listEncoded = jbencode.encode(List.of(10L, "foo"));                // "li10e3:fooe"

String dictEncoded = jbencode.encode(new TreeMap<>(Map.of("foo", 10L)));  // "d3:fooi10ee"
```

Decoding accepts a string as parameter and returns an object of type `Long`, `String`, `List` or `SortedMap`.
The first possible value is returned, the rest of the string is ignored. For example:

```java
Long integer = jbencode.decode("i10e");                             // 10

Long otherInteger = jbencode.decode("i10e3:foo");                   // 10 (ignores "foo")

String string = jbencode.decode("3:foo");                           // "foo"

List<Object> list = jbencode.decode("li10e3:fooe");                 // [10, "foo"]

SortedMap<String, Object> dict = jbencode.decode("d3:foo3:bare");   // {"foo": "bar"}
```

## License

JBencode is licensed under the [GNU General Public License v3.0](https://www.gnu.org/licenses/gpl-3.0.en.html).