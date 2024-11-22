const OwnerController = require("../../app/controllers/OwnerController");
const OwnerRepository = require("../../domain/repositories/OwnerRepository");
const OwnerUseCase = require("../../domain/use-cases/OwnerUseCase");
const db = require("../../infrastructure/database/models");
const { Owner } = db;

const ownerRepository = new OwnerRepository(Owner);
const ownerUseCase = new OwnerUseCase(ownerRepository);
const ownerController = new OwnerController(ownerUseCase);

const ownerHttpHandler = {
    async register(req, res) {
        return ownerController.register(req, res);
    },

    async login(req, res) {
        return ownerController.login(req, res);
    },

    async getProfile(req, res) {
        return ownerController.getProfile(req, res);
    },

    async updateProfile(req, res) {
        return ownerController.updateProfile(req, res);
    },

    async changePassword(req, res) {
        return ownerController.changePassword(req, res);
    },

    async deleteProfile(req, res) {
        return ownerController.deleteProfile(req, res);
    },
};

module.exports = ownerHttpHandler;
