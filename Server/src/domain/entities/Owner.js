class Owner {
    constructor({ owner_id, username, password, full_name, contact }) {
        this.owner_id = owner_id;
        this.username = username;
        this.password = password;
        this.full_name = full_name;
        this.contact = contact;
    }
}

module.exports = Owner;
