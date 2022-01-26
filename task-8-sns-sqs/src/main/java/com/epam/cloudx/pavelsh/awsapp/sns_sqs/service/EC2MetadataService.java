package com.epam.cloudx.pavelsh.awsapp.sns_sqs.service;

import com.epam.cloudx.pavelsh.awsapp.sns_sqs.dto.EC2Metadata;

public interface EC2MetadataService {

  EC2Metadata retrieveMetadata();
}
