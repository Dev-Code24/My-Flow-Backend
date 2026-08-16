package com.myflow.my_flow.services.identity;

import com.myflow.my_flow.commons.ParticipantIdentity;
import com.myflow.my_flow.constants.UserIdentityConstants;
import com.myflow.my_flow.models.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.UUID;

@Service
public class ParticipantIdentityService {
  public ParticipantIdentity resolveIdentity(HttpServletRequest req, HttpServletResponse res) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof User user) {
      return new ParticipantIdentity(user.getId(), true, auth.getName());
    }

    UUID guestId = getGuestId(req);

    if (guestId == null) {
      guestId = UUID.randomUUID();
      res.addCookie(createGuestIdentityCookie(guestId));
    }

    return new ParticipantIdentity(guestId, false, null);
  }

  private UUID getGuestId(HttpServletRequest req) {
    Cookie[] cookies = req.getCookies();

    if (cookies == null) {
      return null;
    }

    String guestId = Arrays.stream(cookies)
        .filter((cookie) -> UserIdentityConstants.USER_UUID_COOKIE.equals(cookie.getName()))
        .map(Cookie::getValue)
        .filter(this::isValidUUID)
        .findFirst()
        .orElse(null);

   if (guestId != null) {
     return UUID.fromString(guestId);
   }

   return null;
  }

  private Cookie createGuestIdentityCookie(UUID guestUserId) {
    Cookie cookie = new Cookie(UserIdentityConstants.USER_UUID_COOKIE, guestUserId.toString());

    cookie.setHttpOnly(true);
    cookie.setPath("/");
    cookie.setMaxAge(Math.toIntExact(UserIdentityConstants.GUEST_IDENTITY_MAX_AGE_SECONDS));
//    cookie.setSecure(true);

    return cookie;
  }

  private boolean isValidUUID(String guestUserUUID) {
    try {
      UUID.fromString(guestUserUUID);
      return true;
    } catch (IllegalArgumentException exception) {
      return false;
    }
  }
}
