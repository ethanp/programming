Citation: most everything is from Wikipedia

Glossary
--------

* **Authentication** --- the process of verifying that "you are who you say you
  are", i.e. assuring the message's origin
* **Authorization** --- the process of verifying that "you are permitted to do
  what you're trying to do"
* **(Public Key/Digital) Certificate** --- an electronic document that uses a **digital signature** to bind a **public key** with ID info; e.g. name, org., address, etc.
    * Used to verify that a public key belongs to an individual
    * Managed with a **public-key infrastructure** scheme
    * Issued by a **certificate authority**, a third-party trusted by both the owner of the certificate, and the party relying upon the certificate to validate the ID of the owner
* **Cryptanalysis** --- the study of breaching cryptographic security systems
    * Mathematical analysis of cryptographic algorithms and exploiting weaknesses in their implementation
    * Given encrypted data, retrieve as much info as possible about the unencrypted data
* **Cryptosystem**
    1. Any computer system that involves cryptography (e.g. eMail)
    2. A suite of algorithms needed to implement a particular form of encryption and decryption, generally of the *asymmetric* variety, e.g.
        1. Key generation
        2. Cipher
            1. Encryption
            2. Decryption
* **Digital Signature** --- mathematical scheme for checking **authentication** and **integrity**
* **Hamming weight** --- the number of symbols in a string that are different from the "zero" of the alphabet used
    * Equivalent to the **Hamming distance** from the all-zero string of the same length
    * E.g. the *l*_1 norm of a bit vector
* **HMAC** -- *Keyed-Hash Message Authentication Code* --- a **MAC** that uses a **keyed hash function**
* **HTTPS** --- layers HTTP on top of **SSL/TLS**
* **Integrity** --- detect accidental and intentional message changes
* **MAC** -- *Message Authentication Code* --- a short piece of information used to **authenticate** a message and to provide **integrity** and **authenticity** assurances on the message.
    * Often created using a **keyed (cryptographic) hash function**
        * I.e. one that accepts as input a **secret key**
* **Modular arithmetic** --- a := b (mod n) if ((a-b) % n) == 0
    * E.g. 38 := 14 (mod 12) because (14-2) % 12 == (38-2) % 12 == 0
    * I guess I might summarize this operation as saying: "a and b are the same distance from being multiples of n"
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
* **Semantically secure** --- when an attacker cannot distinguish two encryptions from each other even if the they chose the corresponding plaintexts
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

* **AES** (Advanced Encryption Standard) --- **symmetric** and popular within the U.S. government
* **Base64** -- encode into alphanumeric+2
    * The last 2 chars might be `[(+,/),(+,-),(-,_),(.,-),(_,:),(!,-), etc.]`
* **RSA** (Rivest, Shamir, Adleman) --- **deterministic, asymmetric** cryptosystem widely used for secure data transmission
    * *Public* encryption key, *Private* decryption key
    * Here, the asymmetry is based on the difficulty of factoring the product of two large prime numbers
    * The public key is the product of two large prime numbers along with an auxiliary value
    * The private key is the two large prime numbers
    * It is an open question whether the "RSA Problem" (break ing RSA) is as 'hard' as the factoring problem
    * **[Operation](http://en.wikipedia.org/wiki/RSA_(algorithm)#Operation)**
        * Choose two *distinct* prime numbers *p* and *q* (at random, of similar bit-length)
        * Compute *n* = *pq* and Phi(*n*) = (*p* - 1)(*q* - 1)
        * Choose an integer *e* s.t. 1 < *e* < Phi(*n*) && *e* and Phi(*n*) are *coprime* (share no prime factors)
            * *e* is the *public key exponent*
            * smaller *e* will be more efficient to compute, but slightly less secure
        * *d* := multiplicative inverse of *e* (modulo Phi(*n*)) [sic]
            * *d* is the *private key exponent*
        * **Encryption**:
            1. Alice sends public key (n, e) to Bob
            2. Bob turns message M into an integer m s.t. 0 ≤ m < n using an agreed-upon reversible **padding scheme** protocol
            3. Bob computes the ciphertext c := m ** e (mod n) and transmits c to Alice
        * **Decryption**:
            1. Alice recovers m from c by computing m := c ** d (mod n)
            2. Now she reverses the padding scheme to recover M from m
        * **Signing Messages**: note that Bob (the public key holder) can **authenticate**
          messages Alice **signs** using her private key. This means that Bob can verify
          both that Alice sent it, and that the message hasn't been tampered with in
          the process.
            * This is *key*
    * One can avoid a lot of the known methods of attack by embedding structured, randomized padding into the value m before encrypting it
        * Padding is very difficult, so there is a patent-expired secure padding scheme known as **RSA-PSS**
        * Padding schemes "are as essential for the security of message signing as they are for message encryption"
    * Integer factorization and the RSA problem
        * For len(n) ≤ 300, n can be factored in a few hours on a PC using free software
        * For len(n) ≥ 2048, you should be safe for a while
        * Shor's algorithm shows that a quantum computer would be able to factor in polynomial time, breaking RSA

