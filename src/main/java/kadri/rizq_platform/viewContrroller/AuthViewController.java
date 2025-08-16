package kadri.rizq_platform.viewContrroller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kadri.rizq_platform.dto.JwtResponse;
import kadri.rizq_platform.dto.LoginRequest;
import kadri.rizq_platform.dto.RegistrationRequestDto;
import kadri.rizq_platform.jwt.JwtUtil;
import kadri.rizq_platform.service.AuthService;
import kadri.rizq_platform.service.RegistrationRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthViewController {

    private final RegistrationRequestService registrationRequestService;
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @GetMapping("/test-error")
    public String testError() {
        throw new IllegalArgumentException(" This is a test error from HtmlExceptionHandler");
    }


    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registrationRequest", new RegistrationRequestDto(
                null, "", "","", "", "", null));
        return "register";
    }

    @PostMapping("/register")
    public String handleRegistration(@ModelAttribute("registrationRequest") @Valid RegistrationRequestDto dto,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            registrationRequestService.submitRequest(dto);
            redirectAttributes.addFlashAttribute("successMessage", "Your request has been submitted. Please wait for admin approval.");
            return "redirect:/auth/register";
        } catch (IllegalArgumentException ex) {
            bindingResult.rejectValue("phoneNumber", "error.registrationRequest", ex.getMessage());
            return "register";
        }
    }
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginRequest", new LoginRequest("", ""));
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(@ModelAttribute("loginRequest") @Valid LoginRequest loginRequest,
                              BindingResult bindingResult,
                              HttpServletResponse response,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "login";
        }

        try {
            JwtResponse jwtResponse = authService.login(loginRequest);

            var cookie = ResponseCookie.from("jwt", jwtResponse.token())
    .httpOnly(true)
    .secure(true) // in prod
    .sameSite("Strict")
    .path("/")
    .maxAge(Duration.ofHours(1))
    .build();
response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());


            String role = jwtUtil.extractRole(jwtResponse.token());
            return role.equals("ADMIN") ? "redirect:/admin/requests" : "redirect:/dashboard";

        } catch (IllegalArgumentException ex) {
            bindingResult.reject("error.login", ex.getMessage());
            return "login";
        }
    }

}

