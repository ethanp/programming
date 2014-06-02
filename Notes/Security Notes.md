latex input:		mmd-article-header
Title:		Notes on Security
Author:		Ethan C. Petuchowski
Base Header Level:		1
latex mode:		memoir
Keywords:		Security, Matthew Koontz, Inherited, Questions
CSS:		http://fletcherpenney.net/css/document.css
xhtml header:		<script type="text/javascript" src="http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML">
</script>
copyright:			2014 Ethan C. Petuchowski
latex input:		mmd-natbib-plain
latex input:		mmd-article-begin-doc
latex footer:		mmd-memoir-footer

Glossary
========

* **Authentication** --- the process of verifying that "you are who you say you
  are", i.e. assuring the message's origin
* **Authorization** --- the process of verifying that "you are permitted to do
  what you're trying to do"
* **(Public Key/Digital) Certificate** --- an electronic document that uses a **digital signature** to bind a **public key** with ID info; e.g. name, org., address, etc.
    * Used to verify that a public key belongs to an individual
    * If you trust the certificate, you also trust that messages signed by the sender’s private key were sent by that person.
    * Managed with a **public-key infrastructure** scheme
    * Issued by a **certificate authority**, a third-party trusted by both the owner of the certificate, and the party relying upon the certificate to validate the ID of the owner
    * To provide a means of determining the legitimacy of a certificate, the
      sender’s certificate is signed by someone else, whose certificate is in
      turn signed by someone else, and so on, forming a chain of trust to a
      certificate that the recipient inherently trusts, called an anchor
      certificate.
* **Checksum** --- allow detection and repair of small number of changes
    * It's goal is similar to a hash function, but the use-case and therefore algorithms used are different
* **Cryptanalysis** --- the study of breaching cryptographic security systems
    * Mathematical analysis of cryptographic algorithms and exploiting weaknesses in their implementation
    * Given encrypted data, retrieve as much info as possible about the unencrypted data
* **Cryptographic hash function** --- takes an arbitrary block of data and returns a
    fixed-size bit string, the *cryptographic hash value*, s.t. any change to the data
    will change the hash value
* **Cryptosystem**
    1. Any computer system that involves cryptography (e.g. eMail)
    2. A suite of algorithms needed to implement a particular form of encryption and decryption, generally of the *asymmetric* variety, e.g.
        1. Key generation
        2. Cipher
            1. Encryption
            2. Decryption
* **Dictionary attack** --- trying a dictionary full of *likely* passwords in an attempt to gain unauthorized access
* **Digest** --- I believe this is the output of a **cryptographic hash function** such as **SHA-1**
* **Hamming weight** --- the number of symbols in a string that are different from the "zero" of the alphabet used
    * Equivalent to the **Hamming distance** from the all-zero string of the same length
    * E.g. the \\(l_{1}\\) norm of a bit vector
* **Handshake** --- automated negotiation process that dynamically sets parameters of a communications chanel before communication begins
    * After establishing connection, before transferring desired information
    * Agreement on: (e.g.) transfer rate, coding alphabet, protocol, etc.
    * Might just say "got the message" over and over, or might say "data was corrupted, please resend"
* **HMAC** --- *Keyed-Hash Message Authentication Code* --- a **MAC** that uses a **keyed hash function**
* **HTTPS** --- layers HTTP on top of **SSL/TLS**
* **Initialization vector** --- a pseudorandom number added to a message before encrypting to make this instance of the encryption unique
* **Integrity** --- detect accidental and intentional message changes
* **Keychain** --- apps ask this thing, via unique identifier, to open things with its key, so the app never actually sees the key itself
* **MAC** --- *Message Authentication Code* --- a short piece of information used to **authenticate** a message and to provide **integrity** and **authenticity** assurances on the message.
    * Often created using a **keyed (cryptographic) hash function**
        * I.e. one that accepts as input a **secret key**
* **Modular arithmetic** --- \\(a \equiv b\\) (mod \\(n\\)) **if** \\(((a-b)\,\%\,n == 0 \\))
    * E.g. \\(\,38 = 14 \,( \mathrm{mod}\, 12) \\) because \\((14-2)\, \% \,12 == (38-2) \,\%\, 12 == 0 \\)
    * I guess I might summarize this operation as saying: "a and b are the same distance from being multiples of n"
* **Nonce** --- arbitrary (generally pseudorandom) number used only once in a cryptographic communication
    * E.g. used to **authenticate** users in a way that can't be reused in **replay attacks**
        * First you ask the server for a nonce, then you use the result to encrypt your **password** and send it
* **Private-key / Symmetric Cryptography** --- uses one key for both encryption and decryption
* **Public-key / Asymmetric Cryptography** --- requires a *secret* **private key** and an *openly available* **public key**
    * **Public key**
        * *Encrypt plaintext*
        * *Verify* a **digital signature**
    * **Private key**
        * *Decrypt ciphertext*
        * *Create* the **digital signature**
    * Computationally infeasible to determine private key from public key
        * So public key can be published, and only those with private key may read the messages
* **Rainbow table attack** --- a *dictionary attack* using a precomputed table of reversed cryptographic hash functions
	* using a **salt** makes this attack unfeasible
* **Replay attack** --- someone catches your request to send money to someone else, and they just keep sending that request over and over
* **Salt** --- random data that is used as an additional input to a one-way function that hashes a password or passphrase
	* Protects against *dictionary* and *rainbow table* attacks
* **Secret Key** --- a piece of information that serves as a parameter to a cryptographic algorithm
* **Semantically secure** --- when an attacker cannot distinguish two encryptions from each other even if the they chose the corresponding plaintexts
* **Signature** --- scheme for checking **authentication** and **integrity**
* **SSL (Secure Sockets Layer) / TLS (Transport Layer Security)** --- cryptographic protocols designed to provide communication security over the Internet
    * **SSL provides an encrypted TCP connection**
    * **data integrity**
    * **endpoint authentication**
    * First they *assure the counterparty*, aka *handshake*, then they exchange a *symmetric key* which is then used to encrypt data sent between them
    * Often uses port 443 for **HTTPS**
    * *Uses:* eCommerce and banking


Notes on Specific Things
========================

Passwords
---------

##### 5/1/14

References:

1. [Coda Hale: How to safely store a password](http://codahale.com/how-to-safely-store-a-password/)
2. [Security Stack Exchange: How to securely hash passwords](http://security.stackexchange.com/questions/211/how-to-securely-hash-passwords)
3. [Wikipedia: bcrypt](http://en.wikipedia.org/wiki/Bcrypt)
4. [Wikipedia: Key derivation function](http://en.wikipedia.org/wiki/Key_derivation_function)
5. [Wikipedia: Password](http://en.wikipedia.org/wiki/Password)
6. [OWASP: Password Storage Cheat Sheet](https://www.owasp.org/index.php/Password_Storage_Cheat_Sheet)

### Recurrent themes

* Use `bcrypt`. E.g. `py-bcrypt` or `jBCrypt`
* Borrow code from someone who knows what they're doing because you don't want to mess this up

##### Hashing Vs. Encryption

**You can't un-hash, but you can *decrypt***

### Why bcrypt?

* General purpose hash functions (e.g. MD5, SHA-X) are too fast to calculate, making brute force attacks easier.
* Salts don't prevent **dictionary attacks** *(see Glossary above)* or brute force attacks
* `bcrypt` uses a variant of the Blowfish encryption algorithm’s keying schedule, and introduces a work factor,
  which **allows you to determine how expensive the hash function will be**
* Instead of cracking a password every 40 seconds, I’d be cracking them every 12 years or so
* `bcrypt` has salts built-in to prevent **rainbow table** attacks

### Salting

* The `salt` is a 128-byte char-string
* You save `"salt:HASH(salt+password)"` into the database
	* Note the `salt` is visible there as plaintext, this is OK as long as you're not just *giving these away*
* This means no one can use a table of pre-hashed strings to attack you
* They *can* find your salt, but then they'd have to compute all the hashes *for each user*

### Some Don'ts

From [Reference 2](http://security.stackexchange.com/questions/211/how-to-securely-hash-passwords)

> For some reason, many developers insist on designing function themselves, and seem to assume that
> "secure cryptographic design" means "throw together every kind of cryptographic or non-cryptographic
> operation that can be thought of". See 
> [this question](http://security.stackexchange.com/questions/25585/is-my-developers-home-brew-password-security-right-or-wrong-and-why)
> for an example. The underlying principle seems to be that the sheer complexity of the resulting
> utterly tangled mess of instruction will befuddle attackers. In practice, though, the developer
> himself will be more confused by his own creation than the attacker.

> **Complexity is bad. Homemade is bad. New is bad.** If you remember that, you'll avoid 99% of
> problems related to password hashing, or cryptography, or even security in general.

### Some Dos

* Use as many iterations as you can tolerate on your server
* Use `/dev/urandom` to obtain salts. **Don't** use the *username*.


Cryptographic Hash Functions
------------------------------------

##### Hashing Vs. Encryption

**You can't un-hash, but you can *decrypt***

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
    * **[Operation](http://en.wikipedia.org/wiki/RSA_(algorithm))**
        * Choose two *distinct* prime numbers *p* and *q* (at random, of similar bit-length)
        * Compute \\(n = pq\\) and \\(\Phi(n) = (p-1)(q-1)\\)
        * Choose an integer \\(e\\) s.t. \\(1 < e < \Phi(n)\\), and also \\(e\\) & \\(\Phi(n)\\) are *coprime* (share no prime factors)
            * *e* is the *public key exponent*
            * smaller *e* will be more efficient to compute, but slightly less secure
        * *d* := multiplicative inverse of *e* (modulo \\(\Phi(n)\\)) [sic]
            * *d* is the *private key exponent*
        * **Encryption**:
            1. Alice sends public key \\((n, e)\\) to Bob
            2. Bob turns message \\(M\\) into an integer m s.t. \\(0 \leq m < n\\) using an agreed-upon reversible **padding scheme** protocol
            3. Bob computes the ciphertext \\(c \equiv {m}^{e}\\) (mod \\(n\\)) and transmits \\(c\\) to Alice
        * **Decryption**:
            1. Alice recovers \\(m\\) from \\(c\\) by computing \\(m \equiv {c}^{d} \;(\mathrm{mod} \; n\\))
            2. Now she reverses the padding scheme to recover \\(M\\) from \\(m\\)
        * **Signing Messages**: note that Bob (the public key holder) can **authenticate**
          messages Alice **signs** using her private key. This means that Bob can verify
          both that Alice sent it, and that the message hasn't been tampered with in
          the process.
            * This is *key*
    * One can avoid a lot of the known methods of attack by embedding structured, randomized padding into the value m before encrypting it
        * Padding is very difficult, so there is a patent-expired secure padding scheme known as **RSA-PSS**
        * Padding schemes "are as essential for the security of message signing as they are for message encryption"
    * Integer factorization and the RSA problem
        * For \\( \mathrm{len}(n)  \leq 300 \\), \\(n\\) can be factored in a few hours on a PC using free software
        * For \\( \mathrm{len}(n) \geq 2048 \\), you should be safe for a while
        * Shor's algorithm shows that a quantum computer would be able to factor in polynomial time, breaking RSA

Big Idea
--------

### Part 1

1. An **HMAC algorithm** (e.g. SHA-1) gives you a nonsense string to put at the end of
   the message so that people know it hasn't been fussed with.
2. An **encryption algorithm** (e.g. RSA) gives you a tool to make your data secret as
   you transmit it, and verify that it was sent by whoever said they sent it.

Notes about Practice
------------------------

Asymmetric encryption is often used for establishing a shared communication
channel. Because asymmetric encryption is computationally expensive, the two
**endpoints often use asymmetric encryption to exchange a symmetric key, and then
use a much faster symmetric encryption algorithm for encrypting and decrypting
the actual data.**

Asymmetric encryption can also be used to establish trust. By encrypting
information with your private key, someone else can read that information with
your public key and be certain that it was encrypted by you.

In addition to the data itself, signing and verifying require two pieces of
information: the appropriate half of a public-private key pair and a digital
certificate.

The sender computes a hash of the message and encrypts it with the private key.
The recipient also computes a hash and then uses the corresponding public key
to decrypt the sender’s hash and compares the hashes. If they are the same, the
data was not modified in transit, and you can safely trust that the data was
sent by the owner of that key. [Ref: Apple][Apple on Public, Private Keys]

### Typical use of Public Key, and Reason it is Naive

1. Alice uses one of the public key algorithms to generate a pair of encryption
   keys: a private key, which she keeps secret, and a public key. She also
   prepares a message to send to Bob.
2. Alice sends the public key to Bob, unencrypted. Because her private key
   cannot be deduced from the public key, doing so does not compromise her private
   key in any way.
3. Alice can now easily prove her identity to Bob (a process known as
   authentication). To do so, she encrypts her message (or any portion of the
   message) using her private key and sends it to Bob.
4. Bob decrypts the message with Alice’s public key. This proves the message
   must have come from Alice, as only she has the private key used to encrypt it.
5. Bob encrypts his message using Alice’s public key and sends it to Alice. The
   message is secure, because even if it is intercepted, no one but Alice has the
   private key needed to decrypt it.
6. Alice decrypts the message with her private key.

#### Problem:
Since encryption and authentication are subjects of great interest in national
security and protecting corporate secrets, some extremely smart people are
engaged both in creating secure systems and in trying to break them. Therefore,
it should come as no surprise that actual secure communication and
authentication procedures are considerably more complex than the one just
described. For example, the authentication method of encrypting the message
with your private key can be got around by a man-in-the-middle attack, in which
someone with malicious intent (usually referred to as Eve in books on
cryptography) intercepts Alice’s original message and replaces it with their
own, so that Bob is using not Alice’s public key, but Eve’s. Eve then
intercepts each of Alice’s messages, decrypts it with Alice’s public key,
alters it (if she wishes), and reencrypts it with her own private key. When Bob
receives the message, he decrypts it with Eve’s public key, thinking that the
key came from Alice.

Although this is a subject much too broad and technical to be covered in detail
in this document, digital certificates and digital signatures can help address
these security problems. These techniques are described later in this chapter.

[Ref: Apple][Apple on Public, Private Keys]

[Apple on Public, Private Keys]: https://developer.apple.com/library/ios/documentation/Security/Conceptual/Security_Overview/CryptographicServices/CryptographicServices.html

HTTPS
-----

#### HTTP Secure

* [Wiki](http://en.wikipedia.org/wiki/HTTP_Secure)
* [Security Stack Exchange](http://security.stackexchange.com/questions/19616/why-is-it-possible-to-sniff-an-https-ssl-request)

In short *https* sessions work this way:

1. Client and server exchange some initial information
2. Using this information the client is able to authenticate the server, making sure it is trust worth
3. The client uses the server's public key to send him an encrypted secret
4. The server decrypts this secret using his private key (only the authenticated server can do that!)
5. Both the client and the server use the secret to locally generate the session symmetric key
6. Now the can talk to each other safely because no one else knows their session symmetric key

If some how along the way a third party is able to get access to the shared
secret he will also be able to generate the session symmetric key and decrypt
the communication.

The problem is that step 2 (authenticating the server) is hard.  SSL is only as
strong as your certificate validation. It all comes down to: Do you accept the
MitM's certificate as valid. The logic of out current browsers is: Accept if
it's signed by a trusted CA or if the user decides to override
