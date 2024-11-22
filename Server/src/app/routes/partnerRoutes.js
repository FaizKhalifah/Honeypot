const express = require("express");
const authMiddleware = require("../middlewares/authMiddleware");
const partnerHttpHandler = require("../../interfaces/http/partnerHttpHandler");

const router = express.Router();

router.post("/add", authMiddleware, (req, res) =>
    partnerHttpHandler.addPartner(req, res)
);
router.get("/", authMiddleware, (req, res) =>
    partnerHttpHandler.getAllPartners(req, res)
);
router.get("/:partnerId", authMiddleware, (req, res) =>
    partnerHttpHandler.getPartnerDetails(req, res)
);
router.put("/:partnerId/product", authMiddleware, (req, res) =>
    partnerHttpHandler.addOrUpdatePartnerProduct(req, res)
);
router.delete("/:partnerId", authMiddleware, (req, res) =>
    partnerHttpHandler.deletePartner(req, res)
);

module.exports = router;
