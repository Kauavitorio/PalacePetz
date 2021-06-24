package co.kaua.palacepetz.Data.Schedule;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ScheduleServices {

    @POST("user/create/schedule")
    Call<DtoSchedule> CreateSchedule(@Body DtoSchedule dtoSchedule);

    @DELETE("user/schedule/cancel/{cd_schedule}/{id_user}/{description}")
    Call<DtoSchedule> CancelSchedule(@Path("cd_schedule") int cd_schedule, @Path("id_user") int id_user, @Path("description") String description);
}
