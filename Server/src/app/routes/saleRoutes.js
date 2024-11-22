const express = require("express");
const authMiddleware = require("../middlewares/authMiddleware");
const saleHttpHandler = require("../../interfaces/http/saleHttpHandler");

const router = express.Router();


router.get("/report/total", authMiddleware, (req, res) =>
    saleHttpHandler.getTotalSalesReport(req, res)
);
router.get("/report/:partnerId", authMiddleware, (req, res) =>
    saleHttpHandler.getMonthlyReport(req, res)
);
router.post("/record/:partnerId", authMiddleware, (req, res) =>
    saleHttpHandler.recordSale(req, res)
);

module.exports = router;
