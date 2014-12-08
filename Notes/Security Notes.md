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

## Glossary

* **Authentication** --- the process of verifying that "you are who you say you
  are", i.e. assuring the message's origin
* **Authorization** --- the process of verifying that "you are permitted to do
  what you're trying to do"
* **Buffer overflow** --- while writing data to a buffer, a program overruns
  the buffer's boundary and overwrites adjacent memory
    * They are the basis of many software vulnerabilities and can be maliciously exploited
        * By e.g. overwriting the return address in a stack frame, meaning that execution will "return" to the attacker's address, which contains his data
    * Bounds checking can prevent buffer overflows
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
* **[Distributed] Denial-of-Service ([D]DOS) attack** --- attempt to make a
  machine or network resource unavailable to its intended users
    * Generally consists of efforts to temporarily or indefinitely interrupt
      or suspend services of a host connected to the Internet.
    * One common method of attack involves saturating the target machine with
      external communications requests, so much so that it cannot respond to
      legitimate traffic, or responds so slowly as to be rendered essentially
      unavailable.
* **Firewall** --- block messages based on origin, content, frequency, etc.
    * e.g. block all traffec from particular hosts or to particular TCP ports
* **Hamming weight** --- the number of symbols in a string that are different from the "zero" of the alphabet used
    * Equivalent to the **Hamming distance** from the all-zero string of the same length
    * E.g. the \\(l_{1}\\) norm of a bit vector
* **Exploit** --- a piece of software, a chunk of data, or a sequence of
  commands that takes advantage of a bug, glitch or vulnerability in order to
  cause unintended or unanticipated behavior to occur on computer software,
  hardware, or something electronic.
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
* **Privilege escalation** --- gain elevated access to resources that are
  normally protected from an application or user
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
* **Spoofing attack** --- one person or program successfully masquerades as another by falsifying data
* **SSL (Secure Sockets Layer) / TLS (Transport Layer Security)** --- cryptographic protocols designed to provide communication security over the Internet
    * **SSL provides an encrypted TCP connection**
    * **data integrity**
    * **endpoint authentication**
    * First they *assure the counterparty*, aka *handshake*, then they exchange a *symmetric key* which is then used to encrypt data sent between them
    * Often uses port 443 for **HTTPS**
    * *Uses:* eCommerce and banking
* **Vulnerability** --- a *security-relevant* **software defect** that can be *exploited*


## Notes on Specific Things

### Buffer Overflow
##### 12/8/14

This example is from the Coursera course "Software Security" video "Buffer Overflow"

    void func(char *arg1) {
        int authenticated = 0;
        char buffer[4]; // quite small
        strcpy(buffer, str);
        if (authenticated) {
            /* do secret stuff */
        }
    }

    int main() {
        char *mystr = "AuthMe!";
        func(mystr);
    }

What happens is `mystr` is bigger than `buffer`, so it overwrites
`authenticated` to be non-zero, meaning that `if (authenticated)` is `true`,
even though the authentication didn't check-out.

### Passwords

##### 5/1/14

References:

1. [Coda Hale: How to safely store a password](http://codahale.com/how-to-safely-store-a-password/)
2. [Security Stack Exchange: How to securely hash passwords](http://security.stackexchange.com/questions/211/how-to-securely-hash-passwords)
3. [OWASP: Password Storage Cheat Sheet](https://www.owasp.org/index.php/Password_Storage_Cheat_Sheet)

#### Recurrent theme

> Don't write your own code for this stuff

#### Why bcrypt?

* It is *slow* to compute, unlike MD5, SHA-X
* *Salts* don't prevent **dictionary attacks** *(see Glossary above)* or brute force attacks
* It has a parameter to allow you to choose the compute requirements
* It has salts built-in to prevent **rainbow table** attacks

#### Salting

* The salt is a **128-char string**
* You save `"salt:HASH(salt+password)"` into the database
    * Note the salt is visible there as plaintext, this is OK as long as you're not just *giving these away*
* This means no one can use a table of pre-hashed strings to attack you
* They *can* find your salt, but then they'd have to compute all the hashes *for each user*

#### Some Don'ts

Paraphrased from
[stackexchange](http://security.stackexchange.com/questions/211/how-to-securely-hash-passwords)

> **Complexity is bad. Homemade is bad. New is bad.** If you remember that, you'll avoid 99% of
> problems related to password hashing, or cryptography, or even security in general.
> Do *not* write a tangled mess of operations and expect that to confuse attackers.

#### Some Dos

* Use as many iterations as you can tolerate on your server
* Use `/dev/urandom` to obtain salts. **Don't** use the *username*.


### Cryptographic Hash Functions

##### Hashing Vs. Encryption

**You can't un-hash, but you can *decrypt***

#### Message-Digest

* **MD5** --- *has had some problems*; produces 128-bit / 16-byte / 32-digit-hex hash value
    * It's not *that* secure, it seems like
* **SHA-1** --- produces 160-bit / 20-byte / 40-digit-hex
    * **Uses** --- encryption (not so secure), data integrity (e.g. Git)
* **SHA-2** --- more secure than SHA-1, really secure.

#### Other Noteworthy Algorithms

* **AES** (Advanced Encryption Standard) --- **symmetric** and popular within the U.S. government
* **Base64** -- encode into alphanumeric+2
    * The last 2 chars might be `[(+,/),(+,-),(-,_),(.,-),(_,:),(!,-), etc.]`

#### RSA
Rivest, Shamir, Adleman --- **deterministic, asymmetric** cryptosystem widely used for secure data transmission

* *Public* encryption key, *Private* decryption key
* Based on the difficulty of factoring the product of two large prime numbers
* Public key --- the product of two large prime numbers and an auxiliary value
* Private key --- the two large prime numbers
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

## Notes about Practice

[Ref: Apple][Apple on Public, Private Keys]

Asymmetric encryption is often used for establishing a shared communication
channel. Because asymmetric encryption is computationally expensive, the two
endpoints often use asymmetric encryption to exchange a symmetric key, and
then use a much faster symmetric encryption algorithm for encrypting and
decrypting the actual data.

Asymmetric encryption can also be used to establish trust. By encrypting
information with your private key, someone else can read that information with
your public key and be certain that it was encrypted by you.

### Typical use of Public Key, and Reason it is Naive

1. Alice generate public/private key pair
2. Alice sends the public key to Bob, unencrypted
3. Alice encrypts her message (or any portion of the message) using her
   private key and sends it to Bob
4. Bob decrypts it with Alice’s public key, proving it came from Alice
5. Bob encrypts his message using Alice’s public key and sends it to Alice.
6. Alice decrypts the message with her private key.

#### Problem:
The authentication method of encrypting the message with your private key can
be got around by a man-in-the-middle attack, in which someone with malicious
intent ("Eve") replaces Alice’s original message with her own, so that Bob
Eve’s instead. Eve then intercepts & alters each of Alice’s messages. When Bob
receives the message, he decrypts it with Eve’s public key, thinking that the
key came from Alice.

[Apple on Public, Private Keys]: https://developer.apple.com/library/ios/documentation/Security/Conceptual/Security_Overview/CryptographicServices/CryptographicServices.html

### HTTPS[ecure]

> *The first step in providing secure services is using HTTP Secure (HTTPS),
> which encrypts your packets.*

#### Overview

1. Client and server exchange an *SSL certificate* in a `.crt` file ($10-300)
2. Now client *authenticates* server (using Web of Trust, scariest part of the process)
3. Client uses the server's public key to send him an encrypted secret
4. Server decrypts this secret using his *private* key (which *only* the authenticated server can do!)
5. Both the client and the server use the secret to locally generate the session symmetric key
6. Now they can talk safely because no one else knows their session symmetric key
    * However, a third party with access to the shared secret will be able to
      generate the session symmetric key and decrypt the communication.
