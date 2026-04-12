package project.backend.hotel_booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.backend.hotel_booking.core.util.ResponseUtil;
import project.backend.hotel_booking.core.util.SuccessResponse;
import project.backend.hotel_booking.model.vo.PartnerInfoManageVO;
import project.backend.hotel_booking.model.vo.PartnerInfoVO;
import project.backend.hotel_booking.service.PartnerInfoService;

import java.util.List;

@RestController
@RequestMapping("/api/partner-info")
public class PartnerInfoController {
    private final PartnerInfoService partnerInfoService;

    public PartnerInfoController(PartnerInfoService partnerInfoService) {
        this.partnerInfoService = partnerInfoService;
    }

    @GetMapping("/partner-pending")
    public SuccessResponse<List<PartnerInfoVO>> getPartnerInfoPending(){
        return ResponseUtil.ok("Get success",partnerInfoService.getAllPartnerPending());
    }


    @PostMapping("/reject/{partnerId}")
    public SuccessResponse<String> rejectPartner(@PathVariable String partnerId){
        partnerInfoService.rejectPartner(partnerId);
        return ResponseUtil.ok("Reject success");
    }

    @PostMapping("/band/{partnerId}")
    public SuccessResponse<String> bandPartner(@PathVariable Long partnerId){
        partnerInfoService.bandPartner(partnerId);
        return ResponseUtil.ok("Band/Active success");
    }

    @GetMapping("/get/all")
    public SuccessResponse<List<PartnerInfoManageVO>> getAllPartnerInfo(){
        return ResponseUtil.ok("Get PartnerInfo success",partnerInfoService.gerAllPartnerInfo());
    }


}
