const express = require("express");
const authMiddleware = require("../middlewares/authMiddleware");
const ownerHttpHandler = require("../../interfaces/http/ownerHttpHandler");

const router = express.Router();

router.post("/register", (req, res) => ownerHttpHandler.register(req, res));
router.post("/login", (req, res) => ownerHttpHandler.login(req, res));
router.get("/profile", authMiddleware, (req, res) =>
    ownerHttpHandler.getProfile(req, res)
);
router.put("/profile", authMiddleware, (req, res) =>
    ownerHttpHandler.updateProfile(req, res)
);
router.put("/change-password", authMiddleware, (req, res) =>
    ownerHttpHandler.changePassword(req, res)
);
router.delete("/profile", authMiddleware, (req, res) =>
    ownerHttpHandler.deleteProfile(req, res)
);

module.exports = router;
