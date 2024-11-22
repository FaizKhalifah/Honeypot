const express = require("express");
const authMiddleware = require("../middlewares/authMiddleware");
const productHttpHandler = require("../../interfaces/http/productHttpHandler");

const router = express.Router();

router.post("/add", authMiddleware, (req, res) =>
    productHttpHandler.addProduct(req, res)
);
router.put("/update/:productId", authMiddleware, (req, res) =>
    productHttpHandler.updateProduct(req, res)
);
router.put("/update-stock/:productId", authMiddleware, (req, res) =>
    productHttpHandler.updateStock(req, res)
);
router.get("/", authMiddleware, (req, res) =>
    productHttpHandler.getAllProducts(req, res)
);
router.get("/:productId", authMiddleware, (req, res) =>
    productHttpHandler.getProductDetails(req, res)
);
router.delete("/:productId", authMiddleware, (req, res) =>
    productHttpHandler.deleteProduct(req, res)
);

module.exports = router;
