package com.jvmops.gumtree.notifications;

import com.jvmops.gumtree.subscriptions.City;
import com.jvmops.gumtree.subscriptions.CityService;
import com.jvmops.gumtree.subscriptions.Subscription;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@Profile("web")
@Controller
@Validated
@RequestMapping("/")
@AllArgsConstructor
public class EmailSubscriptionController {
    private CityService cityService;

    @GetMapping("/subscribe")
    public String subscribe(
            @RequestParam @NotEmpty String city,
            @RequestParam @Email String email,
            RedirectAttributes redirectAttributes
    ) {
        Subscription subscription = Subscription.builder()
                .city(city)
                .email(email)
                .build();
        cityService.start(subscription);
        redirectAttributes.addAttribute("subscribedCity", subscription.getCity());
        return "redirect:/?subscribed";
    }

    @GetMapping("/unsubscribe")
    public String unsubscribeForm(
            @RequestParam @NotEmpty String city,
            Model model
    ) {
        List<String> cities = cityService.cities().stream()
                .map(City::getName)
                .collect(Collectors.toList());
        model.addAttribute("subscription", new Subscription());
        model.addAttribute("cities", cities);
        model.addAttribute("selectedCity", city);
        return "unsubscribe";
    }

    @PostMapping("/unsubscribe")
    public String unsubscribe(
            @Valid Subscription subscription,
            RedirectAttributes redirectAttributes) {
        cityService.cancel(subscription);
        redirectAttributes.addAttribute("unsubscribed");
        redirectAttributes.addAttribute("unsubscribedCity", subscription.getCity());
        return "redirect:/unsubscribe?unsubscribed";
    }

    @PostMapping("/unsubscribehard")
    public String unsubscribehard(
            Subscription subscription) {
        cityService.cancel(subscription);
        return "redirect:/subscriptions";
    }
}
