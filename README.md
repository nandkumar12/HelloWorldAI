# Spring Boot Microservice on AWS Lambda

This project is a Spring Boot 3.3.0 microservice running on AWS Lambda, using DynamoDB for storage and API Gateway for the REST interface.

## Prerequisites
- AWS Account
- GitHub Account
- Java 21 and Maven installed locally
- AWS SAM CLI installed locally (optional, for local testing)

## Step 1: AWS Console Setup
1. **Create an IAM User**:
   - Go to the **IAM Console** in AWS.
   - Create a new user (e.g., `github-actions-user`).
   - Attach the policy `AdministratorAccess` (for simplicity in this demo, though in production you should use more restrictive permissions).
2. **Generate Access Keys**:
   - For the created user, go to the **Security credentials** tab.
   - Click **Create access key**.
   - Select **Command Line Interface (CLI)** and follow the steps.
   - **Save the Access Key ID and Secret Access Key**. You will need them for GitHub.

## Step 2: GitHub Repository Setup
1. **Create a new repository** on GitHub (do not initialize with README or License).
2. **Push your code**:
   ```powershell
   git init
   git add .
   git commit -m "Initial commit"
   git branch -M main
   git remote add origin https://github.com/YOUR_USERNAME/YOUR_REPO_NAME.git
   git push -u origin main
   ```

## Step 3: Configure GitHub Secrets
1. Go to your repository on GitHub.
2. Navigate to **Settings** > **Secrets and variables** > **Actions**.
3. Click **New repository secret** and add the following:
   - `AWS_ACCESS_KEY_ID`: Your AWS Access Key ID.
   - `AWS_SECRET_ACCESS_KEY`: Your AWS Secret Access Key.
4. (Optional) If you want to use a different region, update it in `.github/workflows/aws.yml`.

## Step 4: Automatic Deployment
- Once the secrets are added, every push to the `main` branch will trigger the GitHub Action defined in `.github/workflows/aws.yml`.
- The workflow will:
  1. Build the Java application using Maven.
  2. Build the AWS SAM application.
  3. Deploy the resources to AWS (Lambda, DynamoDB, API Gateway).

## Step 5: Verify Deployment
1. After the GitHub Action finishes successfully, go to the **Actions** tab in GitHub to see the logs.
2. In the **SAM Build and Deploy** step output, look for the `ApiUrl` in the `CloudFormation outputs` section.
3. Use Postman or CURL to test:
   - **GET** `[ApiUrl]/items/hello` (Sanity check)
   - **POST** `[ApiUrl]/items` with JSON body `{"name": "Test Item", "description": "Hello World"}`
   - **GET** `[ApiUrl]/items/{id}` using the ID returned from the POST.

## Project Structure
- `src/`: Spring Boot application code.
- `template.yml`: AWS SAM template defining Cloud resources.
- `.github/workflows/aws.yml`: CI/CD pipeline configuration.
