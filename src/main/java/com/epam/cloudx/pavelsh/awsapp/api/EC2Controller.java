package com.epam.cloudx.pavelsh.awsapp.api;

import com.amazonaws.util.EC2MetadataUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/server-info")
public class EC2Controller {

  @GetMapping
  public ServerInfoDTO getServerInfo() {
    return new ServerInfoDTO(
        EC2MetadataUtils.getEC2InstanceRegion(), EC2MetadataUtils.getAvailabilityZone());
  }
}
