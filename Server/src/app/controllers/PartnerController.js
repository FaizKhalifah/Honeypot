class PartnerController {
    constructor(partnerUseCase) {
        this.partnerUseCase = partnerUseCase;
    }

    async addPartner(req, res) {
        try {
            const partner = await this.partnerUseCase.addPartner(req.body);
            res.status(201).json(partner);
        } catch (error) {
            res.status(400).json({ message: error.message });
        }
    }

    async getAllPartners(req, res) {
        try {
            const partners = await this.partnerUseCase.getAllPartners();
            res.status(200).json(partners);
        } catch (error) {
            res.status(400).json({ message: error.message });
        }
    }

    async getPartnerDetails(req, res) {
        try {
            const { partnerId } = req.params;
            const partner = await this.partnerUseCase.getPartnerDetails(
                partnerId
            );
            if (!partner)
                return res.status(404).json({ message: "Partner not found" });
            res.status(200).json(partner);
        } catch (error) {
            res.status(400).json({ message: error.message });
        }
    }

    async addOrUpdatePartnerProduct(req, res) {
        try {
            const { partnerId } = req.params;
            const { product_id, stockChange } = req.body;
            await this.partnerUseCase.addOrUpdatePartnerProduct(
                partnerId,
                product_id,
                stockChange
            );
            res.status(200).json({
                message: "Partner product stock updated successfully",
            });
        } catch (error) {
            res.status(400).json({ message: error.message });
        }
    }

    async deletePartner(req, res) {
        try {
            const { partnerId } = req.params;
            await this.partnerUseCase.deletePartner(partnerId);
            res.status(200).json({ message: "Partner deleted successfully" });
        } catch (error) {
            res.status(400).json({ message: error.message });
        }
    }
}

module.exports = PartnerController;
