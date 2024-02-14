import * as cdk from 'aws-cdk-lib';
import {Construct} from 'constructs';
import * as s3 from 'aws-cdk-lib/aws-s3';

export class CdkStack extends cdk.Stack {
  constructor(scope: Construct, id: string, props?: cdk.StackProps) {
    super(scope, id, props);
    this.createS3Bucket();
  }
  private createS3Bucket() {
    new s3.Bucket(this, 'my-bucket-duowang', {
      bucketName: 'my-bucket-duowang-01',
      removalPolicy: cdk.RemovalPolicy.DESTROY,  // will destroy the bucket when the stack is destroyed
    });
  }
}