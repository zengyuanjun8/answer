package com.zyj.answer.controller;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/ip")
public class IpController {

    private static final String[] ipHeaders = new String[]{"X-Real-IP", "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP"};

    @GetMapping({"/my", "mine"})
    public Map<String, String> visiterIp(HttpServletRequest request) {
        HashMap<String, String> ipMap = new HashMap<>();
        ipMap.put("remoteAddr", request.getRemoteAddr());
        for (String ipHeader : ipHeaders) {
            String ip = request.getHeader(ipHeader);
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                continue;
            }
            ipMap.put(ipHeader, ip);
        }
        return ipMap;
    }

}
