import objects.*;

import java.util.Scanner;

abstract class FactoryBase {


    public Person createPerson(Scanner reader){
        return new Person(
                Integer.parseInt(reader.nextLine()),
                reader.nextLine(),
                reader.nextLine(),
                reader.nextLine()
        );
    }

    protected Museum createMuseum(Scanner reader) {
        return new Museum(
                Integer.parseInt(reader.nextLine()),
                reader.nextLine(),
                reader.nextLine()
        );
    }

    protected Coin createCoin(ItemBase base, Scanner reader) {
        Coin coin = new Coin();

        coin.setId(base.getId());
        coin.setDiscoveredLocation(base.getDiscoveredLocation());
        coin.setDiscoveredByPersonId(base.getDiscoveredByPersonId());
        coin.setDiscoveredDate(base.getDiscoveredDate());
        coin.setEstimatedAge(base.getEstimatedAge());

        if(base.getMuseumId() != null) {
            coin.setMuseumId(base.getMuseumId());
        }

        coin.setDiameter(Integer.parseInt(reader.nextLine()));
        coin.setMetal(reader.nextLine());

        return coin;
    }

    protected Jewelry createJewelry(ItemBase base, Scanner reader) {
        Jewelry jewelry = new Jewelry();

        jewelry.setId(base.getId());
        jewelry.setDiscoveredLocation(base.getDiscoveredLocation());
        jewelry.setDiscoveredByPersonId(base.getDiscoveredByPersonId());
        jewelry.setDiscoveredDate(base.getDiscoveredDate());
        jewelry.setEstimatedAge(base.getEstimatedAge());

        if(jewelry.getMuseumId() != null) {
            jewelry.setMuseumId(base.getMuseumId());
        }

        jewelry.setType(reader.nextLine());

        jewelry.setValueEstimate(Integer.parseInt(reader.nextLine()));
        jewelry.setFileName(reader.nextLine());

        return jewelry;
    }

    protected Weapon createWeapon(ItemBase base, Scanner reader) {
        Weapon weapon = new Weapon();

        weapon.setId(base.getId());
        weapon.setDiscoveredLocation(base.getDiscoveredLocation());
        weapon.setDiscoveredByPersonId(base.getDiscoveredByPersonId());
        weapon.setDiscoveredDate(base.getDiscoveredDate());
        weapon.setEstimatedAge(base.getEstimatedAge());

        if(weapon.getMuseumId() != null) {
            weapon.setMuseumId(base.getMuseumId());
        }

        weapon.setType(reader.nextLine());

        weapon.setMaterial(reader.nextLine());
        weapon.setWeight(Integer.parseInt(reader.nextLine()));

        return weapon;
    }

}
