package project.backend.hotel_booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.backend.hotel_booking.core.util.ResponseUtil;
import project.backend.hotel_booking.core.util.SuccessResponse;
import project.backend.hotel_booking.model.vo.PartnerInfoVO;
import project.backend.hotel_booking.service.PartnerInfoService;

import java.util.List;

@RestController
@RequestMapping("/api/partner-info")
public class PartnerInfoController {
    private final PartnerInfoService partnerInfoService;

    @Autowired
    public PartnerInfoController(PartnerInfoService partnerInfoService) {
        this.partnerInfoService = partnerInfoService;
    }

    @GetMapping("/partner-pending")
    public SuccessResponse<List<PartnerInfoVO>> getPartnerInfoPending(){
        return ResponseUtil.ok("Get success",partnerInfoService.getAllPartnerPending());
    }

    @PostMapping("/approval/{partnerId}")
    public SuccessResponse<String> approvalPartner(@PathVariable String partnerId){
        partnerInfoService.approvalPartner(partnerId);
        return ResponseUtil.ok("Approval success");
    }

    @PostMapping("/reject/{partnerId}")
    public SuccessResponse<String> rejectPartner(@PathVariable String partnerId){
        partnerInfoService.rejectPartner(partnerId);
        return ResponseUtil.ok("Reject success");
    }

    @PostMapping("/band/{partnerId}")
    public SuccessResponse<String> bandPartner(@PathVariable String partnerId){
        partnerInfoService.bandPartner(partnerId);
        return ResponseUtil.ok("Reject success");
    }

}
