package com.project.devidea.modules.community.interceptor;

import com.project.devidea.infra.config.security.jwt.JwtTokenUtil;
import com.project.devidea.infra.error.exception.EntityNotFoundException;
import com.project.devidea.modules.community.Community;
import com.project.devidea.modules.community.CommunityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CommunityInterceptor implements HandlerInterceptor {

    private final CommunityRepository communityRepository;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String httpMethod = request.getMethod();
        if(httpMethod.equals("POST")) {
            Map<?,?> pathVariables = (Map<?, ?>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            Long id = Long.parseLong((String) pathVariables.get("id"));
            Community community = communityRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("community"));
            String username = jwtTokenUtil.getUsernameFromToken(request.getHeader("Authorization").replace("Bearer ", ""));
            if(!community.getWriter().getEmail().equals(username)) {
                throw new AccessDeniedException("삭제 권한이 없습니다.");
            }
        }
        return true;
    }



}
