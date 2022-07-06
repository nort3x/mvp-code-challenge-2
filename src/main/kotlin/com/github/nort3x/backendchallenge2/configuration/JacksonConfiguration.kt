package com.github.nort3x.backendchallenge2.configuration

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.module.SimpleModule
import com.github.nort3x.backendchallenge2.model.Tile
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

class ElementSerializer : JsonSerializer<Tile>() {
    override fun serialize(value: Tile?, gen: JsonGenerator, serializers: SerializerProvider?) {
        if (value == null)
            gen.writeNull()
        else{
            gen.writeString(value.asGridCoord())
        }
    }
}

class ElementDeserializer : JsonDeserializer<Tile>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): Tile {
        val asString = p.valueAsString
        return Tile.fromString(asString)
    }

}

@Configuration
class JacksonConfiguration {
    @Bean
    fun objectMapper() =
        ObjectMapper().apply {
            findAndRegisterModules()

            registerModule(
                SimpleModule().apply {
                    addSerializer(Tile::class.java, ElementSerializer())
                    addDeserializer(Tile::class.java, ElementDeserializer())
                }
            )
        }

}