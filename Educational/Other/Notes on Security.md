
Glossary
--------

* **Authentication** --- the process of verifying that "you are who you say you
  are", i.e. assuring the message's origin
* **Authorization** --- the process of verifying that "you are permitted to do
  what you're trying to do"
* **Cryptanalysis** --- the study of breaching cryptographic security systems
    * Mathematical analysis of cryptographic algorithms and exploiting weaknesses in their implementation
    * Given encrypted data, retrieve as much info as possible about the unencrypted data
* **Digital Signature** --- mathematical scheme for checking **authentication** and **integrity**
* **HMAC** -- *Keyed-Hash Message Authentication Code* --- a **MAC** that uses a **keyed hash function**
* **HTTPS** --- layers HTTP on top of **SSL/TLS**
* **Integrity** --- detect accidental and intentional message changes
* **MAC** -- *Message Authentication Code* --- a short piece of information used to **authenticate** a message and to provide **integrity** and **authenticity** assurances on the message.
    * Often created using a **keyed (cryptographic) hash function**
        * I.e. one that accepts as input a **secret key**
* **Private-key / Symmetric Cryptography** --- uses one key for both encryption and decryption
* **Public-key / Asymmetric Crpytography** --- requires a *secret* **private key** and an *openly available* **public key**
    * **Public key**
        * *Encrypt plaintext*
        * *Verify* a **digital signature**
    * **Private key**
        * *Decrypt ciphertext*
        * *Create* the **digital signature**
    * Computationally infeasible to determine private key from public key
        * So public key can be published, and only those with private key may read the messages
* **Secret Key** --- a piece of information that serves as a parameter to a cryptographic algorithm
* **SSL (Secure Sockets Layer) / TLS (Transport Layer Security)** --- cryptographic protocols designed to provide communication security over the Internet
    * First they *assure the counterparty*, aka *handshake*, then they exchange a *symmetric key* which is then used to encrypt data sent between them
    * Often uses port 443 for **HTTPS**
    * *Uses:* eCommerce and banking

### Cryptographic Hash Functions

#### Message-Digest

* **MD5** --- *has had some problems*; produces 128-bit / 16-byte / 32-digit-hex hash value
    * **Uses** --- store one-way hash of password (often with **key stretching**)
    * It's not *that* secure, it seems like
* **SHA-1** --- produces 160-bit / 20-byte / 40-digit-hex
    * **Uses** --- encryption, data integrity (e.g. Git)
* **SHA-2** --- more secure than SHA-1, this one's really secure.

### Other Noteworthy Algorithms

* **Base 64** -- encode into alphanumeric+2
    * The last 2 chars might be `[(+,/),(+,-),(-,_),(.,-),(_,:),(!,-), etc.]`
