package com.atharva.auth.email.client;

import com.atharva.auth.email.model.ErrorCodes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "AdminClient", url = "${url.admin}")
public interface AdminServiceClient {

    @RequestMapping(method =  RequestMethod.GET, value = "/auth/verify/{userId}")
    ErrorCodes adminVerified(@PathVariable(name = "userId") String userId);

    @RequestMapping(method = RequestMethod.POST, value = "/auth/reset")
    ErrorCodes resetPassword(@RequestHeader String admin_auth);
}
