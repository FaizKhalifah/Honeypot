class PartnerUseCase {
    constructor(partnerRepository) {
        this.partnerRepository = partnerRepository;
    }

    async addPartner(partnerData) {
        return await this.partnerRepository.createPartner(partnerData);
    }

    async getAllPartners() {
        return await this.partnerRepository.findAllPartners();
    }

    async getPartnerDetails(partnerId) {
        return await this.partnerRepository.findPartnerById(partnerId);
    }

    async addOrUpdatePartnerProduct(partnerId, productId, stockChange) {
        return await this.partnerRepository.addOrUpdatePartnerStock(
            partnerId,
            productId,
            stockChange
        );
    }

    async deletePartner(partnerId) {
        return await this.partnerRepository.deletePartner(partnerId);
    }
}

module.exports = PartnerUseCase;
