package info.gratour.common.rest

import org.springframework.core.MethodParameter
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.{HandlerMethodArgumentResolver, ModelAndViewContainer}

class UserSessionResolver(clazz: Class[_]) extends HandlerMethodArgumentResolver{
  override def supportsParameter(parameter: MethodParameter): Boolean = {
    parameter.getParameterType.equals(clazz)
  }

  override def resolveArgument(parameter: MethodParameter, mavContainer: ModelAndViewContainer, webRequest: NativeWebRequest, binderFactory: WebDataBinderFactory): AnyRef = {
    SecurityContextHolder.getContext.getAuthentication
  }
}
