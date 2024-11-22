class OwnerRepository {
    constructor(ownerModel) {
        this.ownerModel = ownerModel;
    }

    async createOwner(ownerData) {
        return await this.ownerModel.create(ownerData);
    }

    async findOwnerByUsername(username) {
        return await this.ownerModel.findOne({ where: { username } });
    }

    async findOwnerById(ownerId) {
        return await this.ownerModel.findByPk(ownerId);
    }

    async updateOwner(ownerId, updatedData) {
        return await this.ownerModel.update(updatedData, {
            where: { owner_id: ownerId },
        });
    }

    async deleteOwner(ownerId) {
        return await this.ownerModel.destroy({ where: { owner_id: ownerId } });
    }
}

module.exports = OwnerRepository;
