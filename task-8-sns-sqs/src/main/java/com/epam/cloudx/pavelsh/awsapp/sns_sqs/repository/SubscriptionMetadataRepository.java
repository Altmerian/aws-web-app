package com.epam.cloudx.pavelsh.awsapp.sns_sqs.repository;

import com.epam.cloudx.pavelsh.awsapp.sns_sqs.repository.model.SubscriptionMetadata;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface SubscriptionMetadataRepository extends Repository<SubscriptionMetadata, Long> {

  void save(SubscriptionMetadata subscriptionMetadata);

  Optional<SubscriptionMetadata> getByEmail(String email);
}
