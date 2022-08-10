package fr.robotv2.robotclan.serializable;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.*;

public class SerializableInventory implements java.io.Serializable {

    private transient Inventory inventory;

    public SerializableInventory() {
        this.inventory = Bukkit.createInventory(null, InventoryType.CHEST, "Stockage");
    }

    public Inventory getInventory() {
        return inventory;
    }

    @Serial
    private void writeObject(ObjectOutputStream oos) throws IOException {
        final String contents = toBase64(inventory.getContents());
        oos.writeUTF(contents);
        oos.close();
    }

    @Serial
    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        final ItemStack[] contents = fromBase64(ois.readUTF());
        ois.close();

        if(inventory == null) {
            this.inventory = Bukkit.createInventory(null, InventoryType.CHEST, "Stockage");
        }

        this.inventory.setContents(contents);
    }

    private String toBase64(ItemStack[] items) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            // Write the size of the inventory
            dataOutput.writeInt(items.length);

            // Save every element in the list
            for (ItemStack item : items) {
                dataOutput.writeObject(item);
            }

            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (IOException e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    private ItemStack[] fromBase64(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];

            // Read the serialized inventory
            for (int i = 0; i < items.length; i++) {
                items[i] = (ItemStack) dataInput.readObject();
            }

            dataInput.close();
            return items;
        } catch (IOException | ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }
}
