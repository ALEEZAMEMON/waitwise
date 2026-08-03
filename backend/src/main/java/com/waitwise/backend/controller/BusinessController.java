package com.waitwise.backend.controller;
import java.util.List;
import com.waitwise.backend.dto.BusinessRequest;
import com.waitwise.backend.dto.BusinessResponse;
import com.waitwise.backend.service.BusinessService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/businesses")
@RequiredArgsConstructor
public class BusinessController {

    private final BusinessService businessService;

    @PostMapping
    public BusinessResponse createBusiness(@Valid @RequestBody BusinessRequest request) {
        return businessService.createBusiness(request);
    }
    @GetMapping
    public List<BusinessResponse> getAllBusinesses() {
        return businessService.getAllBusinesses();
    }
    @GetMapping("/{id}")
    public BusinessResponse getBusinessById(@PathVariable Long id) {
        return businessService.getBusinessById(id);
    }
    @PutMapping("/{id}")
    public BusinessResponse updateBusiness(
            @PathVariable Long id,
            @Valid @RequestBody BusinessRequest request
    ) {
        return businessService.updateBusiness(id, request);
    }
    @DeleteMapping("/{id}")
    public String deleteBusiness(@PathVariable Long id) {

        businessService.deleteBusiness(id);

        return "Business deleted successfully";
    }
}