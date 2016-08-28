package controllers;

import akka.dispatch.MessageDispatcher;
import com.fasterxml.jackson.databind.JsonNode;
import dispatchers.AkkaDispatcher;
import models.SensorEntity;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import static play.libs.Json.toJson;


/**
 * Created by jd.torres11 on 27/08/2016.
 */
public class SensorController extends Controller {

    public CompletionStage<Result> getSensores() {
        MessageDispatcher jdbcDispatcher = AkkaDispatcher.jdbcDispatcher;

        return CompletableFuture.
                supplyAsync(() -> { return SensorEntity.FINDER.all(); } ,jdbcDispatcher)
                .thenApply(sensores -> {return ok(toJson(sensores));}
                );
    }

    public CompletionStage<Result> getSensor(Long idP) {
        MessageDispatcher jdbcDispatcher = AkkaDispatcher.jdbcDispatcher;

        return CompletableFuture.
                supplyAsync(() -> { return SensorEntity.FINDER.byId(idP); } ,jdbcDispatcher)
                .thenApply(sensores -> {return ok(toJson(sensores));}
                );
    }
    public CompletionStage<Result> createSensor(){
        MessageDispatcher jdbcDispatcher = AkkaDispatcher.jdbcDispatcher;
        JsonNode n = request().body().asJson();
        SensorEntity sensor = Json.fromJson( n , SensorEntity.class ) ;
        return CompletableFuture.supplyAsync(
                ()->{
                    sensor.save();
                    return sensor;
                }
        ).thenApply(
                sensores -> {
                    return ok(Json.toJson(sensores));
                }
        );
    }
    public CompletionStage<Result> deleteSensor(Long idP){
        MessageDispatcher jdbcDispatcher = AkkaDispatcher.jdbcDispatcher;

        return CompletableFuture.supplyAsync(
                ()->{
                    SensorEntity sensor = SensorEntity.FINDER.byId(idP);
                    sensor.delete();
                    return sensor;
                }
        ).thenApply(
                sensores -> {
                    return ok(Json.toJson(sensores));
                }
        );
    }
    public CompletionStage<Result> updateSensor( Long idP){
        MessageDispatcher jdbcDispatcher = AkkaDispatcher.jdbcDispatcher;
        JsonNode n = request().body().asJson();
        SensorEntity sensor = Json.fromJson( n , SensorEntity.class ) ;
        SensorEntity antiguo = SensorEntity.FINDER.byId(idP);

        return CompletableFuture.supplyAsync(
                ()->{
                    antiguo.setId(sensor.getId());
                    antiguo.setCampo(sensor.getCampo());
                    antiguo.setTipo(sensor.getTipo());
                    antiguo.update();
                    return antiguo;
                }
        ).thenApply(
                sensores -> {
                    return ok(Json.toJson(sensores));
                }
        );
    }
}