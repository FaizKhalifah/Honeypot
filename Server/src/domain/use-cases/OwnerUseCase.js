const bcrypt = require("bcryptjs");
const jwt = require("jsonwebtoken");

class OwnerUseCase {
    constructor(ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    async register(ownerData) {
        const hashedPassword = await bcrypt.hash(ownerData.password, 10);
        ownerData.password = hashedPassword;
        return await this.ownerRepository.createOwner(ownerData);
    }

    async login(username, password) {
        const owner = await this.ownerRepository.findOwnerByUsername(username);
        if (!owner) throw new Error("Invalid username or password");

        const isMatch = await bcrypt.compare(password, owner.password);
        if (!isMatch) throw new Error("Invalid username or password");

        const token = jwt.sign(
            { ownerId: owner.owner_id },
            process.env.JWT_SECRET,
            { expiresIn: "1h" }
        );
        return { owner, token };
    }

    async getProfile(ownerId) {
        return await this.ownerRepository.findOwnerById(ownerId);
    }

    async updateProfile(ownerId, updatedData) {
        await this.ownerRepository.updateOwner(ownerId, updatedData);
        return await this.ownerRepository.findOwnerById(ownerId);
    }

    async changePassword(ownerId, currentPassword, newPassword) {
        const owner = await this.ownerRepository.findOwnerById(ownerId);
        const isMatch = await bcrypt.compare(currentPassword, owner.password);
        if (!isMatch) throw new Error("Current password is incorrect");

        const hashedPassword = await bcrypt.hash(newPassword, 10);
        await this.ownerRepository.updateOwner(ownerId, {
            password: hashedPassword,
        });
    }

    async deleteProfile(ownerId) {
        return await this.ownerRepository.deleteOwner(ownerId);
    }
}

module.exports = OwnerUseCase;
