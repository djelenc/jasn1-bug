# Bug when encoding of ENUMERATED inside SEQUENCE

This project contains a simple example that demonstrates a bug while encoding an ASN.1 sequence message that contains
an `ENUMERATED` field.

The message format is the following
```asn
David DEFINITIONS ::= BEGIN

MyMessage ::= [APPLICATION 10] IMPLICIT SEQUENCE {
  value ENUMERATED { invalid-parameters (0), internal-error (1) },
  message PrintableString
}

END
```


Once passed through the [jasn1-compiler](https://www.openmuc.org/asn1/download/), it generates 
`MyMessage.java`.

If one tries to do an encode-decode cycle on such a message, an `IOException` is raised claiming `Tag does
not match the mandatory sequence element tag`. 

A closer look reveals that the tag of the embedded `ENUMERATED value` filed is encoded with number `2` 
and not `10`, as suggested by [ASN.1](https://www.obj-sys.com/asn1tutorial/node10.html). Consequently,
when trying to decode such a message, an error is thrown.

If we manually change the tag number to `10`, the decoding finishes without errors.

Tu run the `App.java`, issue `mvn clean compile exec:java` and the output is the following

```
...
Encoded SEQUENCE{value: 0, message: a} as [106, 6, 2, 1, 0, 19, 1, 97]
Could not decode [106, 6, 2, 1, 0, 19, 1, 97]: Tag does not match the mandatory sequence element tag.
Decoded [106, 6, 10, 1, 0, 19, 1, 97] as SEQUENCE{value: 0, message: a}
....
```