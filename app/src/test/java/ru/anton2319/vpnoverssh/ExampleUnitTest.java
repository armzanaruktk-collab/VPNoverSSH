package ru.anton2319.vpnoverssh;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testSshProfileCopyAsNew() {
        ru.anton2319.vpnoverssh.data.SSHConnectionProfile original = new ru.anton2319.vpnoverssh.data.SSHConnectionProfile(
                "128.199.200.1", 22, "root", ru.anton2319.vpnoverssh.data.SSHConnectionProfile.AuthenticationType.PASSWORD
        );
        original.setProfileName("Main-Server");
        original.setPassword("secret123");
        original.setConnectionMode("PAYLOAD");
        original.setPayloadUuid("payload-abc");

        ru.anton2319.vpnoverssh.data.SSHConnectionProfile copied = original.copyAsNew("New-Server-Backup");

        assertNotNull(copied.uuid);
        assertNotEquals(original.uuid, copied.uuid);
        assertEquals("New-Server-Backup", copied.getProfileName());
        assertEquals("128.199.200.1", copied.getServerIP());
        assertEquals(22, copied.getServerPort());
        assertEquals("root", copied.getUsername());
        assertEquals("secret123", copied.getPassword());
        assertEquals("PAYLOAD", copied.getConnectionMode());
        assertEquals("payload-abc", copied.getPayloadUuid());
    }

    @Test
    public void testSshProfileDefaultName() {
        ru.anton2319.vpnoverssh.data.SSHConnectionProfile p = new ru.anton2319.vpnoverssh.data.SSHConnectionProfile();
        p.setServerIP("1.2.3.4");
        p.setProfileName("");
        assertEquals("1.2.3.4", p.getProfileName());
    }
}