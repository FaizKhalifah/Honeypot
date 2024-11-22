const PartnerController = require("../../app/controllers/PartnerController");
const PartnerRepository = require("../../domain/repositories/PartnerRepository");
const PartnerUseCase = require("../../domain/use-cases/PartnerUseCase");
const {
    Partner,
    PartnerStock,
    Product,
} = require("../../infrastructure/database/models");

const partnerRepository = new PartnerRepository(Partner, PartnerStock, Product);
const partnerUseCase = new PartnerUseCase(partnerRepository);
const partnerController = new PartnerController(partnerUseCase);

const partnerHttpHandler = {
    async addPartner(req, res) {
        return partnerController.addPartner(req, res);
    },

    async getAllPartners(req, res) {
        return partnerController.getAllPartners(req, res);
    },

    async getPartnerDetails(req, res) {
        return partnerController.getPartnerDetails(req, res);
    },

    async addOrUpdatePartnerProduct(req, res) {
        return partnerController.addOrUpdatePartnerProduct(req, res);
    },

    async deletePartner(req, res) {
        return partnerController.deletePartner(req, res);
    },
};

module.exports = partnerHttpHandler;
