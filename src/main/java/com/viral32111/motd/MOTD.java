package com.viral32111.motd;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import com.destroystokyo.paper.event.server.PaperServerListPingEvent;

import java.io.File;
import java.nio.file.Files;

@SuppressWarnings( "unused" )
public class MOTD extends JavaPlugin implements Listener {
	private Component messageOfTheDay;

	@Override public void onEnable() {
		File messageFile = new File( getDataFolder(), "message.json" );
		if ( !messageFile.exists() ) saveResource( "message.json", false );

		try {
			messageOfTheDay = GsonComponentSerializer.gson().deserialize( Files.readString( messageFile.toPath() ) );
		} catch ( Exception exception ) {
			exception.printStackTrace();
		}

		getLogger().info( "Message is %s".formatted( PlainTextComponentSerializer.plainText().serialize( messageOfTheDay ).replace( "\n", " " ) ) );

		getServer().getPluginManager().registerEvents( this, this );
	}

	@EventHandler public void onPaperServerListPing( PaperServerListPingEvent event ) {
		getLogger().info( "Ping from %s:%d using protocol version %d".formatted(
			event.getClient().getAddress().getAddress().getHostAddress(),
			event.getClient().getAddress().getPort(),
			event.getClient().getProtocolVersion()
		) );

		event.motd( messageOfTheDay );
	}
}
