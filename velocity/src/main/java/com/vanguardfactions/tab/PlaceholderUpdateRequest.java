package com.vanguardfactions.tab;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.neznamy.tab.bridge.shared.message.outgoing.UpdateRelationalPlaceholder;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PlaceholderUpdateRequest {

  private UUID playerId;
  private List<UpdateRelationalPlaceholder> updates;

}
