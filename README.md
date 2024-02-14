# Top K most frequent words in a file
## Tech Stack
- Spring Boot 3.1.5
- Spring Security
- Java 17
- AWS S3
- AWS Cognito
- Redis
- Swagger3
- Junit5
## Spring Security
- Use AWS Cognito as a User Authentication Carrier
1. Suppose you already have a User pool in AWS Cognito
2. Create a User and add it to the User Pool
3. Create Group: ADMIN and USER (as user role names)
4. Add users to the appropriate Group
5. Create an Authorization Code Grant type App Client inside
6. Postman request token steps (skip)
- Configures the following formats in application.yml <font color=#008000>. well-known/jwks.json</font>
```yaml 
- spring.security.oauth2.resourceserver.jwt.jwk-set-uri=https://cognito-idp.{region}.amazonaws.com/{userPoolId}/.well-known/jwks.json
```
- Customized exception handling for JWT invalid (authenticationEntryPoint) and role invalid (accessDeniedHandler) cases, respectively
- In Controller, you can specify '@RolesAllowed' for API access
## AWS S3
- I used **TransferManager** to download files from S3 bucket to local because of the requirement of Assignment.
Then the file is read and analyzed. (Note that TransferManager has been deprecated in AWS SDK for Java 2.x)
- As an alternative, we can also use **S3AsyncClient** (asynchronous client), which does not block call threads when performing an operation.
- So if we are using AWS SDK for Java **2.x** and want to do asynchronous operations, we should use **S3AsyncClient** instead of **TransferManager**.
- Both of these ways I implemented (S3InfrastructureService.java).
- We assume that in your S3 there is a bucket called **my-bucket-duowang-01** with an **epassi** folder for storing your text files (which can be modified in application.yml).
  - You need to upload the file to bucket beforehand
## Redis
- I use local Redis as cache, `txtFileName + k` as Redis key.
- You can modify the configuration in `application.yml`.
## Swagger3
- URL：http://localhost:8080/epassi-txt-freq/index.html
  - Can be defined in `application.yml`.
## Unit test
- I developed a unit test method for Service and Controller.
  - Since the methods in the class are similar, I only write parts.
## exception handling
- This project customizes and encapsulates exception handling.
- You can add and modify exception enumerations in `ExceptionEnum.java`.
## CDK
- I used CDK (`typescript`) as an implementation of infrastructure.
- Only S3 Bucket has been created here ATM.
## Algorithm Efficiency
### For core algorithms, I used two ways: HashMap and Trie
#### 1. Use `HashMap` to store the frequency of each word
- Core algorithms rely primarily on Java's HashMap and Stream API.
- The time complexity of this method is roughly O (n log n) from the point of view of time complexity.
  - Because the method first traverses the entire text string (which is the O (n) operation, where n is the length of the text string).
  - Then sort the frequency of the words (this is the O (n log n) operation).
  - Therefore, the total time complexity is O (n + n log n), simplified to O (n log n).
- From the point of view of spatial complexity, the spatial complexity of this method is O (n), where n is the number of words in the text string.
  - This is because the method needs to create a HashMap to store each word and its frequency.
  - The size of the HashMap is proportional to the number of words.
- From the point of view of algorithms and data structures,
  - This method uses HashMap to store words and their frequency.
  - This is a very efficient data structure that completes insertion and lookup operations in constant time.
  - In addition, the method uses Java's Stream API for sorting and filtering operations.
#### 2. Use `Trie` to store the frequency of each word
- The time complexity of this method is roughly O (n) from the point of view of time complexity.
  - Because the method first traverses the entire text string (which is the O (n) operation, where n is the length of the text string).
  - Then insert and find each word (this is the O (1) operation).
  - Therefore, the total time complexity is O (n).
- From the point of view of spatial complexity, the spatial complexity of this method is O (n), where n is the number of words in the text string.
  - This is because the method needs to create a Trie to store each word and its frequency, and the Trie size is proportional to the number of words.
#### In both of these ways, I implemented it in `TxtFreqAnalyzerService.java` and invoke it in the Controller layer.