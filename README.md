# File Processing System
This repo contains design & implementation of file processing system.
Following is the problem statement for the same. 

### Problem Statement
A file picker gives the user the ability to choose files or folders from their Google Drive and upload it to a backend system. E.g., users can choose files and folders like in the UI below and press selected to start the upload process. Note that users can upload any files that they have permission on. This includes files owned by them as well files shared with them by other users.

![filePickerExampleOne.png](readme-assets/filePickerExampleOne.png)
![filePickerExampleTwo.png](readme-assets/filePickerExampleTwo.png)

The backend system allows a developer to write asynchronous processing logic on the files metadata and their content before they are ingested in a storage system maintained by the backend system. For every file that is correctly ingested, the user gets a signal in the front-end like the one below:

![filePickerExampleThree.png](readme-assets/filePickerExampleThree.png)

After the upload, processing and ingestion is completed via the UI:
Users should be able to read the files that they own or others have shared with them.
Users should not be able to read stale files (deleted at sources, changes in permissions). Files metadata and content is automatically synced with source.
Developers should add newer types of asynchronous transforms for files that are ingested by the user and are synced regularly.

# High Level Design
#### Note: We'll use [Google Picker API](https://developers.google.com/drive/picker/guides/overview) to access user's files from Google Drive.
![ReadSystemWithCDN.png](readme-assets/FileProcessingSystemCDN.png)

## How to generate following AWS secrets
#### AWS Personal access Tokens to access S3
```bash
export PERSONAL_AWS_ACCESS_KEY_ID=yourkey
export PERSONAL_AWS_ACCESS_KEY_SECRET=yoursecret
```
1. Create a user in IAM with full S3 access (we can choose policies to have more fine grained control)
2. Once the user is created, get into the `Security Credentials` settings for user
3. Create access keys

#### Cloudfront tokens & domain to serve content via CDN
```bash
export AWS_CLOUDFRONT_KEY_PAIR_ID=your-key-pair-id
export AWS_CLOUDFRONT_PRIVATE_KEY=your-private-key
export AWS_CLOUDFRONT_DOMAIN=your-domain
```
You can refer to [this tutorial](https://dev.to/aws-builders/setup-cloudfront-amazon-s3-to-deliver-objects-on-the-web-apps-securely-efficiently-2gnk) to have your content served via cloudfront. 
In this implementation, I have added signed URL generation logic which creates temporary CDN URLs with TTL of 10 mins. 
To achieve the same, we need `AWS_CLOUDFRONT_KEY_PAIR_ID` & `AWS_CLOUDFRONT_PRIVATE_KEY`.

You can go through [this AWS tutorial](https://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/PrivateContent.html) to know more about using signed URLs to serve private content. 
To generate above keys, you can refer to [this tutorial](https://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/private-content-trusted-signers.html#choosing-key-groups-or-AWS-accounts) from AWS. 

##### Steps:
1. create public & private keys
```bash
openssl genrsa -out private_key.pem 2048
openssl rsa -pubout -in private_key.pem -out public_key.pem
```
2. upload public key to cloudfront distribution & create key group, check [this documentation](https://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/private-content-trusted-signers.html#private-content-creating-cloudfront-key-pairs) for your reference.
The ID of the public key you created on cloudfront becomes `AWS_CLOUDFRONT_KEY_PAIR_ID`
3. check the private key
```bash
cat private_key.pem
```
4. copy the contents of the file to a string, replace`new lines` with `spaces`, & 
remove lines containing `-----BEGIN PRIVATE KEY-----` & `-----END PRIVATE KEY-----`.
The final key would be something like this `MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCnoK+F3ZLd77ZI mr2dQaiUQInz1PvKtemE1VmZHpsddDZh/0qDVBnqm42Z9T9sY7/9H3EVYqUmBCiQ Bm/uYNNjukjBY8mPsJV8Rz/9nfBA8Ox1Scr+tErW0Aq/yAvkK1vz0zXgrKJTkhRV xgZ4kllW0WXZPcliLEaRYw==`
I have removed multiple line in above key for safety purposes. This key will become your `AWS_CLOUDFRONT_PRIVATE_KEY`

## How to run
1. Make sure all the above environment variables are set. 
2. Open four terminals & execute them one by one.
2. Run UI app
```bash
cd platform-ui
python3 -m http.server 8090
```
3. Run transformation service
```bash
cd transformation-service
./gradlew bootRun
```
4. Run transformation executor service
```bash
cd transformation-executor-service
./gradlew bootRun
```
5. Run WebSocket server service
```bash
cd websocket-server
./gradlew bootRun
```
6. Open `http://localhost:8090`
7. Authenticate yourself & choose a file
8. Connect using postman to websocket (Socket.io) & see the signed URL for your file.
9. You can also use the postman collection present in the repo to get the transformation result.


# Notes
![FilePickerAndReadSystem2.jpg](readme-assets/FilePickerAndReadSystem2.jpg)
![FilePickerAndReadSystem3.jpg](readme-assets/FilePickerAndReadSystem3.jpg)
![FilePickerAndReadSystem4.jpg](readme-assets/FilePickerAndReadSystem4.jpg)
![FilePickerAndReadSystem5.jpg](readme-assets/FilePickerAndReadSystem5.jpg)
![FilePickerAndReadSystem6.jpg](readme-assets/FilePickerAndReadSystem6.jpg)
![FilePickerAndReadSystem7.jpg](readme-assets/FilePickerAndReadSystem7.jpg)
![FilePickerAndReadSystem8.jpg](readme-assets/FilePickerAndReadSystem8.jpg)
![FilePickerAndReadSystem9.jpg](readme-assets/FilePickerAndReadSystem9.jpg)